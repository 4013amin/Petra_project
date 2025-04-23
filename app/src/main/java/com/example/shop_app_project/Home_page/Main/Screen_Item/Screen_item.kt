@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.shop_app_project.Home_page.Main.Screen_Item

import UserPreferences
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.shop_app_project.R
import com.example.shop_app_project.data.models.Profile.UserProfile
import com.example.shop_app_project.data.models.product.ProductModel
import com.example.shop_app_project.data.view_model.SavedProductsViewModel
import com.example.shop_app_project.data.view_model.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    product: ProductModel,
    onBackClick: () -> Unit,
    navController: NavController
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = product.name,
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.blueM)
                ),
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White, RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                // Image Slider
                ImageSlider(
                    images = product.images,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shadow(6.dp, RoundedCornerShape(16.dp))
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Product Info Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        InfoRow(
                            title = "نام محصول",
                            value = product.name,
                            icon = Icons.Outlined.ShoppingCart
                        )
                        Divider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = Color(0xFFEEEEEE)
                        )
                        InfoRow(
                            title = "شهر",
                            value = product.city,
                            icon = Icons.Outlined.LocationOn
                        )
                        Divider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = Color(0xFFEEEEEE)
                        )
                        InfoRow(
                            title = "فروشنده",
                            value = product.nameUser,
                            icon = Icons.Outlined.Person
                        )
                        Divider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = Color(0xFFEEEEEE)
                        )
                        InfoRow(
                            title = "قیمت",
                            value = "${product.price} تومان",
                            icon = Icons.Outlined.ShoppingCart
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Description Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "توضیحات",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF212121),
                            modifier = Modifier.padding(bottom = 8.dp),
                            textAlign = TextAlign.End
                        )
                        Text(
                            text = product.description,
                            fontSize = 16.sp,
                            color = Color(0xFF757575),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Contact Button
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:${product.phone}")
                        }
                        ContextCompat.startActivity(context, intent, null)
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(56.dp)
                        .shadow(6.dp, RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                    elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "تماس",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "تماس: ${product.phone}",
                            fontSize = 16.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Chat Button
                Button(
                    onClick = {
                        val receiverPhone = product.phone
                        val userPhone = UserPreferences.getInstance(context).getUserPhone()
                        navController.navigate("chat/$userPhone/$receiverPhone")
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(56.dp)
                        .shadow(6.dp, RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                    elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.chat),
                            contentDescription = "چت",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "چت با فروشنده",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// Updated InfoRow with Icon
@Composable
fun InfoRow(title: String, value: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF1976D2),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                fontSize = 14.sp,
                color = Color(0xFF757575),
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                fontSize = 16.sp,
                color = Color(0xFF212121),
                fontWeight = FontWeight.Normal
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//private fun ShowButton() {
//    val sampleProduct = ProductModel(
//        name = "محصول تستی",
//        images = listOf("https://via.placeholder.com/300"),  // لینک نمونه تصویر
//        city = "تهران",
//        nameUser = "فروشنده تستی",
//        price = 100,
//        description = "این یک توضیح تستی است.",
//        phone = "09123456789",
//        id = 0,
//        address = "daihdwi",
//        family = "dadaw",
//        image = "Image",
//        created_at = "dawd",
//        updated_at = "dadwa"
//    )
//    val navController = rememberNavController()
//    ProductDetailScreen(product = sampleProduct, onBackClick = {}, navController)
//}


@Composable
fun InfoRow(title: String, value: String) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End
        )
    }
}

@Composable
fun ImageSlider(images: List<String>, modifier: Modifier) {
    val pagerState = com.google.accompanist.pager.rememberPagerState()
    val openDialog = remember { mutableStateOf(false) }
    val selectedImage = remember { mutableStateOf("") }

    Column {
        com.google.accompanist.pager.HorizontalPager(
            count = images.size,
            state = pagerState,
            modifier = Modifier
                .height(300.dp)
                .padding(15.dp)
        ) { page ->
            AsyncImage(
                model = "https://petshopdjango.liara.run${images[page]}",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .clickable {
                        selectedImage.value = "https://petshopdjango.liara.run${images[page]}"
                        openDialog.value = true
                    },
                onError = {
                    Log.e(
                        "ProductDetailScreen",
                        "Error loading image: ${it.result.throwable?.message}"
                    )
                },
                onSuccess = {
                    Log.d("ProductDetailScreen", "Image loaded successfully")
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        PagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp)
        )
    }

    if (openDialog.value) {
        ImageViewerDialog(imageUrl = selectedImage.value, onDismiss = { openDialog.value = false })
    }
}

@Composable
fun ImageViewerDialog(imageUrl: String, onDismiss: () -> Unit) {

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.8f))
                .clickable(onClick = onDismiss)
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
fun PagerIndicator(
    pagerState: com.google.accompanist.pager.PagerState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pagerState.pageCount) { index ->
            val color = if (pagerState.currentPage == index) Color.Black else Color.Gray
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color, shape = RoundedCornerShape(50))
                    .padding(2.dp)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.R)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductForm(navController: NavController) {
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var isFormSubmitted by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "ثبت آگهی جدید",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.blueM)
                ),
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.rectangle),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Form Content
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White, RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    val context = LocalContext.current
                    val userViewModel: UserViewModel = viewModel()

                    var name by remember { mutableStateOf("") }
                    var description by remember { mutableStateOf("") }
                    var price by remember { mutableStateOf("") }
                    var phone by remember { mutableStateOf("") }
                    var userName by remember { mutableStateOf("") }
                    var images by remember { mutableStateOf<List<Uri>>(emptyList()) }
                    var selectedCity by remember { mutableStateOf("تهران") }
                    var expanded by remember { mutableStateOf(false) }

                    val cities = listOf(
                        "تهران",
                        "شیراز",
                        "اصفهان",
                        "مشهد",
                        "تبریز",
                        "کرج",
                        "قم",
                        "رشت",
                        "کرمان",
                        "اهواز",
                        "زاهدان",
                        "اردبیل",
                        "اراک",
                        "بوشهر",
                        "قزوین",
                        "همدان",
                        "یاسوج",
                        "یزد",
                        "گرگان",
                        "خرم‌آباد",
                        "سنندج",
                        "زنجان",
                        "ایلام",
                        "بیرجند",
                        "بندرعباس",
                        "سبزوار",
                        "سمنان",
                        "کاشان",
                        "کیش"
                    ).sorted()

                    val imagePickerLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.GetMultipleContents()
                    ) { uris ->
                        if (uris.size <= 10) {
                            images = uris
                        } else {
                            Toast.makeText(
                                context,
                                "حداکثر 10 تصویر می‌توانید انتخاب کنید.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    // Image Picker Section
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(
                                onClick = { imagePickerLauncher.launch("image/*") },
                                modifier = Modifier
                                    .fillMaxWidth(0.7f)
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xFF1976D2
                                    )
                                )
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_add_a_photo_24),
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("افزودن تصاویر", color = Color.White, fontSize = 16.sp)
                                }
                            }

                            if (images.isNotEmpty()) {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(top = 16.dp)
                                ) {
                                    items(images) { uri ->
                                        Image(
                                            painter = rememberImagePainter(uri),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(90.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .border(
                                                    2.dp,
                                                    Color(0xFF1976D2),
                                                    RoundedCornerShape(8.dp)
                                                )
                                                .shadow(4.dp, RoundedCornerShape(8.dp))
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Form Fields Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Product Name
                            FormField(
                                label = "نام محصول",
                                value = name,
                                onValueChange = { name = it },
                                isError = isFormSubmitted && name.isBlank(),
                                errorMessage = "این فیلد الزامی است"
                            )

                            // Description
                            FormField(
                                label = "توضیحات محصول",
                                value = description,
                                onValueChange = { description = it },
                                isError = isFormSubmitted && description.isBlank(),
                                errorMessage = "این فیلد الزامی است",
                                maxLines = 3
                            )

                            // Seller Name
                            FormField(
                                label = "نام فروشنده",
                                value = userName,
                                onValueChange = { userName = it },
                                isError = isFormSubmitted && userName.isBlank(),
                                errorMessage = "این فیلد الزامی است"
                            )

                            // Price
                            FormField(
                                label = "قیمت محصول",
                                value = price,
                                onValueChange = { price = it },
                                isError = isFormSubmitted && price.isBlank(),
                                errorMessage = "این فیلد الزامی است",
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )

                            // Phone
                            FormField(
                                label = "شماره تماس",
                                value = phone,
                                onValueChange = { phone = it },
                                isError = isFormSubmitted && phone.isBlank(),
                                errorMessage = "این فیلد الزامی است",
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                            )

                            // City Dropdown
                            Text(
                                text = "شهر",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF212121),
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            ExposedDropdownMenuBox(
                                expanded = expanded,
                                onExpandedChange = { expanded = !expanded }
                            ) {
                                OutlinedTextField(
                                    value = selectedCity,
                                    onValueChange = {},
                                    readOnly = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(),
                                    trailingIcon = {
                                        Icon(
                                            imageVector = if (expanded) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropDown,
                                            contentDescription = null,
                                            tint = Color(0xFF1976D2)
                                        )
                                    },
                                    colors = TextFieldDefaults.colors(
                                        focusedIndicatorColor = Color(0xFF1976D2),
                                        unfocusedIndicatorColor = Color(0xFF757575),
                                        cursorColor = Color(0xFF1976D2)
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    modifier = Modifier.heightIn(max = 300.dp)
                                ) {
                                    cities.forEach { city ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = city,
                                                    fontSize = 16.sp,
                                                    color = Color(0xFF212121)
                                                )
                                            },
                                            onClick = {
                                                selectedCity = city
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Submit Button
                    Button(
                        onClick = {
                            isFormSubmitted = true
                            if (name.isNotBlank() && description.isNotBlank() && price.isNotBlank() &&
                                phone.isNotBlank() && userName.isNotBlank() && images.isNotEmpty()
                            ) {
                                scope.launch {
                                    try {
                                        isLoading = true
                                        userViewModel.sendProduct(
                                            name,
                                            description,
                                            price,
                                            phone,
                                            userName,
                                            selectedCity,
                                            images,
                                            context
                                        )
                                        userViewModel.job?.join()
                                        navController.popBackStack()
                                    } catch (e: Exception) {
                                        Log.e("AddProductForm", "Error: ${e.message}")
                                        Toast.makeText(
                                            context,
                                            "خطا در ارسال درخواست: ${e.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } finally {
                                        isLoading = false
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(56.dp)
                            .shadow(6.dp, RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (name.isNotBlank() && description.isNotBlank() &&
                                price.isNotBlank() && phone.isNotBlank() && userName.isNotBlank() && images.isNotEmpty()
                            ) Color(0xFF1976D2) else Color(0xFFB0BEC5)
                        ),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                text = "ثبت آگهی",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

// Reusable Form Field Composable
@Composable
fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    errorMessage: String,
    maxLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Column {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF212121),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = Color(0xFF212121)),
            isError = isError,
            maxLines = maxLines,
            keyboardOptions = keyboardOptions,
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color(0xFF1976D2),
                unfocusedIndicatorColor = Color(0xFF757575),
                cursorColor = Color(0xFF1976D2),
                errorIndicatorColor = Color.Red
            )
        )
        if (isError) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesPage(
    viewModel: SavedProductsViewModel,
    navController: NavController
) {
    val savedProducts = viewModel.savedProducts

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "محصولات ذخیره‌شده",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.blueM)
                ),
            )
        },
    ) { paddingValues ->
        if (savedProducts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_note_add_24),
                        contentDescription = "Empty",
                        tint = Color(0xFF1976D2),
                        modifier = Modifier
                            .size(100.dp)
                            .clickable { navController.navigate("home") }
                            .background(Color(0xFFE3F2FD), CircleShape)
                            .padding(24.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "هنوز محصولی ذخیره نکرده‌اید!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212121)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "محصولات مورد علاقه خود را اینجا ذخیره کنید.",
                        fontSize = 16.sp,
                        color = Color(0xFF757575),
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(savedProducts) { product ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate("singleProduct/${product.id}") }
                            .shadow(6.dp, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Product Image
                            Box(
                                modifier = Modifier
                                    .size(110.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFEEEEEE))
                                    .shadow(4.dp, RoundedCornerShape(12.dp))
                            ) {
                                if (product.images.isNotEmpty()) {
                                    AsyncImage(
                                        model = "https://petshopdjango.liara.run${product.images.first()}",
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            // Product Details
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = product.name,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF212121),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = product.description,
                                    fontSize = 14.sp,
                                    color = Color(0xFF757575),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "${product.price} تومان",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1976D2)
                                )
                            }

                            // Delete Button
                            IconButton(onClick = { viewModel.removeSavedProduct(product) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Remove",
                                    tint = Color(0xFFE53935),
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProductsScreen(
    userViewModel: UserViewModel = viewModel(),
    phone: String,
    context: Context,
    navController: NavController
) {
    val userProducts by userViewModel.userProducts
    val userProfile by userViewModel.userProfile

    LaunchedEffect(Unit) {
        userViewModel.getUserProducts(phone, context)
        userViewModel.getProfileViewModel(context, phone)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "محصولات من",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.blueM)
                ),
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                userProfile?.let {
                    val defaultName =
                        if (it.name.isNullOrBlank()) "نام خود را تکمیل کنید" else it.name
                    val defaultImage =
                        if (it.image.isNullOrBlank()) "/images/default_profile.png" else it.image
                    ProfileSection(
                        userProfile = it.copy(name = defaultName, image = defaultImage),
                        navController = navController
                    )
                } ?: run {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Warning",
                                tint = Color(0xFFE53935),
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "لطفاً اطلاعات پروفایل خود را تکمیل کنید.",
                                fontSize = 16.sp,
                                color = Color(0xFF212121),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            if (userProducts.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_note_add_24),
                                contentDescription = "Empty",
                                tint = Color(0xFF1976D2),
                                modifier = Modifier
                                    .size(80.dp)
                                    .background(Color(0xFFE3F2FD), CircleShape)
                                    .padding(20.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "هنوز محصولی اضافه نکرده‌اید.",
                                fontSize = 18.sp,
                                color = Color(0xFF212121),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            } else {
                items(userProducts) { product ->
                    ProductItem(
                        product = product,
                        onDeleteClick = {
                            userViewModel.deleteUserProduct(phone, product.id, context)
                        },
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun ProductItem(
    product: ProductModel,
    onDeleteClick: () -> Unit,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("singleProduct/${product.id}") }
            .shadow(6.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product Image
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFEEEEEE))
                    .shadow(4.dp, RoundedCornerShape(12.dp))
            ) {
                if (product.images.isNotEmpty()) {
                    AsyncImage(
                        model = "https://petshopdjango.liara.run${product.images.first()}",
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Product Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = product.description,
                    fontSize = 14.sp,
                    color = Color(0xFF757575),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${product.price} تومان",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1976D2)
                )
            }

            // Delete Button
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Remove",
                    tint = Color(0xFFE53935),
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
fun ProfileSection(userProfile: UserProfile, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { navController.navigate("profileDetail") }
            .shadow(6.dp, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Image
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(Color(0xFF6A1B9A), Color(0xFFAB47BC)),
                            radius = 150f
                        )
                    )
                    .border(3.dp, Color.White, CircleShape)
                    .shadow(4.dp, CircleShape)
            ) {
                val imageUrl = if (userProfile.image.startsWith("http")) {
                    userProfile.image
                } else {
                    "https://petshopdjango.liara.run${userProfile.image}"
                }

                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Profile Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // User Name
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = userProfile.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "مشاهده جزئیات",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF757575)
                )
            }


            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "جزئیات بیشتر",
                tint = Color(0xFF1976D2),
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileDetailScreen(
    userViewModel: UserViewModel = viewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val userProfile by userViewModel.userProfile
    var isRefreshing by remember { mutableStateOf(false) }
    val refreshState = rememberSwipeRefreshState(isRefreshing)
    val userPreferences = UserPreferences.getInstance(context)
    val phone = userPreferences.getUserPhone()

    SwipeRefresh(
        state = refreshState,
        onRefresh = {
            isRefreshing = true
            userViewModel.getProfileViewModel(context, phone.toString())
            isRefreshing = false
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "پروفایل من",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorResource(id = R.color.blueM)
                    )
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))

                    // Profile Image with Modern Effects
                    Box(
                        modifier = Modifier
                            .size(160.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(Color(0xFF6A1B9A), Color(0xFFAB47BC)),
                                    radius = 200f
                                )
                            )
                            .border(5.dp, Color.White, CircleShape)
                            .shadow(8.dp, CircleShape)
                    ) {
                        val imageUrl = if (userProfile?.image?.startsWith("http") == true) {
                            userProfile!!.image
                        } else {
                            "https://petshopdjango.liara.run${userProfile?.image}"
                        }

                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "Profile Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // User Name
                    userProfile?.let {
                        Text(
                            text = it.name,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF212121),
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }

                // User Info Card
                item {
                    userProfile?.let { profile ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                profile.bio?.let { bio ->
                                    ProfileInfoRow(
                                        icon = Icons.Outlined.Info,
                                        label = "بیوگرافی",
                                        value = bio
                                    )
                                    Divider(
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        color = Color(0xFFEEEEEE)
                                    )
                                }

                                phone?.let { phone ->
                                    ProfileInfoRow(
                                        icon = Icons.Outlined.Phone,
                                        label = "شماره تماس شما",
                                        value = phone
                                    )
                                }

                                Divider(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    color = Color(0xFFEEEEEE)
                                )

                                profile.address?.let { address ->
                                    ProfileInfoRow(
                                        icon = Icons.Outlined.LocationOn,
                                        label = "آدرس",
                                        value = address
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))

                    // Edit Profile Button
                    Button(
                        onClick = { navController.navigate("EditProfileScreen") },
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(56.dp)
                            .shadow(6.dp, RoundedCornerShape(12.dp)),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                        shape = RoundedCornerShape(12.dp),
                        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 4.dp)
                    ) {
                        Text(
                            text = "ویرایش پروفایل",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun ProfileInfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF1976D2),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                fontSize = 14.sp,
                color = Color(0xFF757575),
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                fontSize = 16.sp,
                color = Color(0xFF212121),
                fontWeight = FontWeight.Normal
            )
        }
    }
}

fun uriToFile(context: Context, uri: Uri): File? {
    val contentResolver = context.contentResolver
    val fileName = "temp_image_${System.currentTimeMillis()}.jpg"
    val file = File(context.cacheDir, fileName)

    return try {
        contentResolver.openInputStream(uri)?.use { inputStream ->
            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        Log.d("EditProfile", "فایل کپی شد: ${file.absolutePath}")
        file
    } catch (e: IOException) {
        Log.e("EditProfile", "خطا در تبدیل URI به فایل: ${e.message}")
        null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    userViewModel: UserViewModel = viewModel(),
    navController: NavController,
    context: Context = LocalContext.current
) {
    val userProfile = userViewModel.userProfile.value
    val scope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf(userProfile?.name ?: "") }
    var bio by remember { mutableStateOf(userProfile?.bio ?: "") }
    var address by remember { mutableStateOf(userProfile?.address ?: "") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isRefreshing by remember { mutableStateOf(false) }
    val refreshState = rememberSwipeRefreshState(isRefreshing)
    val userPreferences = UserPreferences.getInstance(context)
    val phone = userPreferences.getUserPhone()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> imageUri = uri }

    SwipeRefresh(
        state = refreshState,
        onRefresh = {
            isRefreshing = true
            userViewModel.getProfileViewModel(context, phone.toString())
            isRefreshing = false
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "ویرایش پروفایل",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "بازگشت",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorResource(id = R.color.blueM)
                    )
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))

                    // Profile Image
                    Box(
                        modifier = Modifier
                            .size(160.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(Color(0xFF6A1B9A), Color(0xFFAB47BC)),
                                    radius = 200f
                                )
                            )
                            .border(5.dp, Color.White, CircleShape)
                            .shadow(8.dp, CircleShape)
                            .clickable { imagePickerLauncher.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        val currentImageUrl = if (userProfile?.image?.startsWith("http") == true) {
                            userProfile.image
                        } else {
                            "https://petshopdjango.liara.run${userProfile?.image ?: ""}"
                        }

                        AsyncImage(
                            model = imageUri ?: currentImageUrl,
                            contentDescription = "عکس پروفایل",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Overlay Icon for Edit
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(40.dp)
                                .background(Color(0xFF1976D2), CircleShape)
                                .border(2.dp, Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "تغییر عکس",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Input Fields Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text("نام") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = TextFieldDefaults.colors(
                                    focusedIndicatorColor = Color(0xFF1976D2),
                                    cursorColor = Color(0xFF1976D2),
                                    unfocusedIndicatorColor = Color(0xFF757575)
                                )
                            )

                            OutlinedTextField(
                                value = bio,
                                onValueChange = { bio = it },
                                label = { Text("بیوگرافی") },
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 3,
                                shape = RoundedCornerShape(12.dp),
                                colors = TextFieldDefaults.colors(
                                    focusedIndicatorColor = Color(0xFF1976D2),
                                    cursorColor = Color(0xFF1976D2),
                                    unfocusedIndicatorColor = Color(0xFF757575)
                                )
                            )

                            OutlinedTextField(
                                value = address,
                                onValueChange = { address = it },
                                label = { Text("آدرس") },
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 2,
                                shape = RoundedCornerShape(12.dp),
                                colors = TextFieldDefaults.colors(
                                    focusedIndicatorColor = Color(0xFF1976D2),
                                    cursorColor = Color(0xFF1976D2),
                                    unfocusedIndicatorColor = Color(0xFF757575)
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Save Button
                    Button(
                        onClick = {
                            scope.launch {
                                userViewModel.editProfileViewModel(
                                    context = context,
                                    name = name,
                                    bio = bio,
                                    address = address,
                                    imageUri = imageUri,
                                    phone = phone.toString()
                                )
                                withContext(Dispatchers.Main) { navController.popBackStack() }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(56.dp)
                            .shadow(6.dp, RoundedCornerShape(12.dp)),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                        shape = RoundedCornerShape(12.dp),
                        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 4.dp)
                    ) {
                        Text(
                            text = "ذخیره تغییرات",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}