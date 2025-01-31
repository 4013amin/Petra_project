package com.example.shop_app_project.Home_page.Main.Screen_Item

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
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
import com.example.shop_app_project.Home_page.Main.ProductItem
import com.example.shop_app_project.R
import com.example.shop_app_project.data.models.product.ProductModel
import com.example.shop_app_project.data.view_model.SavedProductsViewModel
import com.example.shop_app_project.data.view_model.UserViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(product: ProductModel, onBackClick: () -> Unit) {
    val scroller = rememberScrollState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = product.name, color = Color.White) },
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
                )
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scroller)
                    .padding(paddingValues)
                    .background(color = Color.White)
            ) {
                ImageSlider(
                    images = product.images,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        InfoRow(title = "نام محصول", value = product.name)
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        InfoRow(title = "شهر", value = product.city)
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        InfoRow(title = "فروشنده", value = product.nameUser)
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        InfoRow(title = "قیمت", value = "${product.price} تومان")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "توضیحات",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp),
                            textAlign = TextAlign.End
                        )
                        Text(
                            text = product.description,
                            fontSize = 16.sp,
                            color = Color.Gray,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // دکمه تماس
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:${product.phone}")
                        }
                        ContextCompat.startActivity(context, intent, null)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6200EE)
                    )
                ) {
                    Text(
                        text = "تماس با فروشنده: ${product.phone}",
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    )
}

@Composable
fun InfoRow(title: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
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
                model = "https://petshopdjango.liara.run/${images[page]}",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .clickable {
                        selectedImage.value = "https://petshopdjango.liara.run/${images[page]}"
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

@ExperimentalMaterial3Api
@Composable
fun AddProductForm(navController: NavController) {
    val scroller = rememberScrollState()
    val scope = rememberCoroutineScope()
    var IsLoading by remember { mutableStateOf(false) }
    var isFormSubmitted by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "ثبت آگهی جدید", color = Color.White) },
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
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.rectangle),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                        color = Color.White
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = Color.White)
                                .padding(16.dp)
                                .verticalScroll(scroller),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
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
                                contract = ActivityResultContracts.GetMultipleContents(),
                                onResult = { uris ->
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
                            )

                            // Form UI
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .background(color = Color.White),
                                horizontalAlignment = Alignment.End
                            ) {
                                IconButton(
                                    onClick = {
                                        imagePickerLauncher.launch("image/*")
                                    },
                                    modifier = Modifier.padding(top = 16.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_add_a_photo_24),
                                        contentDescription = null,
                                        modifier = Modifier.size(25.dp)
                                    )
                                }

                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(top = 16.dp)
                                ) {
                                    items(images) { uri ->
                                        Image(
                                            painter = rememberImagePainter(uri),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(80.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .border(
                                                    2.dp,
                                                    MaterialTheme.colorScheme.primary,
                                                    RoundedCornerShape(8.dp)
                                                )
                                        )
                                    }
                                }

                                // Name Field
                                Text(
                                    text = "نام محصول",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(5.dp)
                                )
                                OutlinedTextField(
                                    value = name,
                                    onValueChange = { name = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    isError = isFormSubmitted && name.isBlank() // نمایش خطا فقط پس از ارسال فرم
                                )
                                if (isFormSubmitted && name.isBlank()) {
                                    Text(
                                        text = "این فیلد الزامی است.",
                                        color = Color.Red,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(15.dp))

                                // Description Field
                                Text(
                                    text = "توضیحات محصول",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(5.dp)
                                )
                                OutlinedTextField(
                                    value = description,
                                    onValueChange = { description = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    isError = isFormSubmitted && description.isBlank() // نمایش خطا فقط پس از ارسال فرم
                                )
                                if (isFormSubmitted && description.isBlank()) {
                                    Text(
                                        text = "این فیلد الزامی است.",
                                        color = Color.Red,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(15.dp))

                                // User Name Field
                                Text(
                                    text = "نام فروشنده",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(5.dp)
                                )
                                OutlinedTextField(
                                    value = userName,
                                    onValueChange = { userName = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    isError = isFormSubmitted && userName.isBlank() // نمایش خطا فقط پس از ارسال فرم
                                )
                                if (isFormSubmitted && userName.isBlank()) {
                                    Text(
                                        text = "این فیلد الزامی است.",
                                        color = Color.Red,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(15.dp))

                                // Price Field
                                Text(
                                    text = "قیمت محصول",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(5.dp)
                                )
                                OutlinedTextField(
                                    value = price,
                                    onValueChange = { price = it },
                                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.fillMaxWidth(),
                                    isError = isFormSubmitted && price.isBlank() // نمایش خطا فقط پس از ارسال فرم
                                )
                                if (isFormSubmitted && price.isBlank()) {
                                    Text(
                                        text = "این فیلد الزامی است.",
                                        color = Color.Red,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(15.dp))

                                // Phone Field
                                Text(
                                    text = "شماره تماس",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(5.dp)
                                )
                                OutlinedTextField(
                                    value = phone,
                                    onValueChange = { phone = it },
                                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                                    modifier = Modifier.fillMaxWidth(),
                                    isError = isFormSubmitted && phone.isBlank() // نمایش خطا فقط پس از ارسال فرم
                                )
                                if (isFormSubmitted && phone.isBlank()) {
                                    Text(
                                        text = "این فیلد الزامی است.",
                                        color = Color.Red,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(15.dp))

                                // City Field
                                Text(
                                    text = "شهر",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(5.dp)
                                )
                                ExposedDropdownMenuBox(
                                    expanded = expanded,
                                    onExpandedChange = { expanded = !expanded }
                                ) {
                                    OutlinedTextField(
                                        value = selectedCity,
                                        onValueChange = { },
                                        readOnly = true,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .menuAnchor(),
                                        trailingIcon = {
                                            Icon(
                                                imageVector = if (expanded) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropDown,
                                                contentDescription = null
                                            )
                                        }
                                    )

                                    DropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false }
                                    ) {
                                        cities.forEach { city ->
                                            DropdownMenuItem(
                                                text = { Text(text = city, fontSize = 16.sp) },
                                                onClick = {
                                                    selectedCity = city
                                                    expanded = false
                                                }
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(30.dp))

                                Button(
                                    onClick = {
                                        isFormSubmitted = true // فرم ارسال شده است
                                        if (name.isNotBlank() &&
                                            description.isNotBlank() &&
                                            price.isNotBlank() &&
                                            phone.isNotBlank() &&
                                            images.isNotEmpty()
                                        ) {
                                            scope.launch {
                                                try {
                                                    IsLoading = true
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
                                                    IsLoading = false
                                                }
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 45.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (name.isNotBlank() &&
                                            description.isNotBlank() &&
                                            price.isNotBlank() &&
                                            phone.isNotBlank() &&
                                            images.isNotEmpty()
                                        ) {
                                            colorResource(id = R.color.blueM)
                                        } else {
                                            Color.Gray
                                        }
                                    ),
                                    enabled = !IsLoading && name.isNotBlank() &&
                                            description.isNotBlank() &&
                                            price.isNotBlank() &&
                                            phone.isNotBlank() &&
                                            images.isNotEmpty()
                                ) {
                                    if (IsLoading) {
                                        CircularProgressIndicator(
                                            color = Color.White,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    } else {
                                        Text(text = "ثبت آگهی")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@ExperimentalMaterial3Api
@Preview
@Composable
private fun showAddForm() {
    val navController = rememberNavController()
    AddProductForm(navController = navController)
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
                title = { Text(text = "محصولات ذخیره‌شده", color = Color.White) },
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
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFF5F5F5))
            ) {
                if (savedProducts.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_note_add_24),
                                contentDescription = "Empty",
                                tint = colorResource(id = R.color.blueM),
                                modifier = Modifier
                                    .size(80.dp)
                                    .clickable {
                                        navController.navigate("home")
                                    }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "هنوز محصولی ذخیره نکرده‌اید!",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "محصولات مورد علاقه خود را اینجا ذخیره کنید.",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(savedProducts) { product ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        navController.navigate("singleProduct/${product.id}")
                                    },
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(100.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color.LightGray)
                                    ) {
                                        if (product.images.isNotEmpty()) {
                                            AsyncImage(
                                                model = "https://petshopdjango.liara.run/${product.images.first()}",
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = product.name,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = product.description,
                                            fontSize = 14.sp,
                                            color = Color.Gray,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "$${product.price} تومان",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF00C853)
                                        )
                                    }

                                    IconButton(
                                        onClick = { viewModel.removeSavedProduct(product) }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Remove",
                                            tint = Color.Red
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProductsScreen(
    userViewModel: UserViewModel,
    phone: String,
    context: Context,
    navController: NavController
) {
    val userProducts by userViewModel.userProducts
    LaunchedEffect(Unit) {
        userViewModel.getUserProducts(phone, context)
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
                )
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFFAFAFA))
            ) {
                if (userProducts.isEmpty()) {
                    Text(
                        text = "هنوز محصولی اضافه نکرده‌اید.",
                        modifier = Modifier.align(Alignment.Center),
                        fontSize = 18.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
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
    )
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
            .clickable {
                navController.navigate("singleProduct/${product.id}")
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFEEEEEE))
            ) {
                if (product.images.isNotEmpty()) {
                    AsyncImage(
                        model = "https://petshopdjango.liara.run/${product.images.first()}",
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF333333),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = product.description,
                    fontSize = 14.sp,
                    color = Color(0xFF666666),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$${product.price} تومان",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00C853)
                )
            }

            IconButton(
                onClick = { onDeleteClick() },
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Remove",
                    tint = Color(0xFFE53935)
                )
            }
        }
    }
}