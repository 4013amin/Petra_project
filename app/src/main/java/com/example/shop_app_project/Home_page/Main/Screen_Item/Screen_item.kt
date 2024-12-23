package com.example.shop_app_project.Home_page.Main.Screen_Item

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.shop_app_project.R
import com.example.shop_app_project.data.models.product.ProductModel
import com.example.shop_app_project.data.utils.UtilsRetrofit
import com.example.shop_app_project.data.view_model.UserViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(product: ProductModel) {
    val images = product.image
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = product.name, color = Color.Black) },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color(0xFF03A9F4))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Image section
            AsyncImage(
                model = "http://192.168.1.110:2020/${images}",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(12.dp)),
                onError = {
                    Log.e("ProductItem", "Error loading image: ${it.result.throwable?.message}")
                },
                onSuccess = {
                    Log.d("ProductItem", "Image loaded successfully")
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Information section with rows
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.End
            ) {
                InfoRow(value = product.name)
                InfoRow(value = product.nameUser)
                InfoRow(value = product.city)
                InfoRow(value = product.family)
                InfoRow(value = "${product.price} تومان", color = Color.Green)
                InfoRow(value = product.address)
                InfoRow(value = product.description)
                InfoRow(value = product.phone)
                InfoRow(value = product.created_at)
                InfoRow(value = product.updated_at)
            }
        }
    }
}

@Composable
fun InfoRow(value: String, color: Color = Color.Black) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}


@Composable
fun ImageSlider(images: List<String>) {
    val pagerState = com.google.accompanist.pager.rememberPagerState()

    Column {
        com.google.accompanist.pager.HorizontalPager(
            count = images.size,
            state = pagerState,
            modifier = Modifier.height(200.dp)
        ) { page ->
            // Use rememberImagePainter to load the image from the URL string
            Image(
                painter = rememberImagePainter(images[page]),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(16f / 9f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Pager Indicator
        PagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp)
        )
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

//@RequiresApi(Build.VERSION_CODES.O)
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AddProductForm(navController: NavController) {
//    val context = LocalContext.current
//    val userViewModel: UserViewModel = viewModel()
//    val provinces = mapOf(
//        "تهران" to listOf("تهران", "ری", "شمیرانات"),
//        "اصفهان" to listOf("اصفهان", "کاشان", "خمینی‌شهر"),
//        "خراسان رضوی" to listOf("مشهد", "نیشابور", "تربت حیدریه")
//    )
//
//    var selectedProvince by remember { mutableStateOf("") }
//    var selectedCity by remember { mutableStateOf("") }
//    val cities = provinces[selectedProvince] ?: emptyList()
//
//    var name by remember { mutableStateOf("") }
//    var description by remember { mutableStateOf("") }
//    var price by remember { mutableStateOf("") }
//    var phone by remember { mutableStateOf("") }
//    var images by remember { mutableStateOf<List<Uri>>(emptyList()) }
//    Box(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        Image(
//            painter = painterResource(id = R.drawable.rectangle),
//            contentDescription = null,
//            contentScale = ContentScale.Crop,
//            modifier = Modifier.fillMaxSize()
//        )
//
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 150.dp)
//                .padding(bottom = 80.dp)
//        ) {
//            Surface(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .weight(1f),
//                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
//                color = Color.White
//            ) {
//                Column(
//                    modifier = Modifier.padding(16.dp),
//                    horizontalAlignment = Alignment.End
//                ) {
//
//                    OutlinedTextField(
//                        value = name,
//                        onValueChange = { name = it },
//                        label = { Text("نام محصول", textAlign = TextAlign.End) },
//                        placeholder = { Text("نام محصول را وارد کنید", textAlign = TextAlign.End) },
//                        modifier = Modifier.fillMaxWidth(),
//                        textStyle = androidx.compose.ui.text.TextStyle(
//                            textAlign = TextAlign.End
//                        )
//                    )
//                    OutlinedTextField(
//                        value = phone,
//                        onValueChange = { phone = it },
//                        label = { Text("نام محصول", textAlign = TextAlign.End) },
//                        placeholder = { Text("تلفن  خود را وارد کنید", textAlign = TextAlign.End) },
//                        modifier = Modifier.fillMaxWidth(),
//                        textStyle = androidx.compose.ui.text.TextStyle(
//                            textAlign = TextAlign.End
//                        )
//                    )
//
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    OutlinedTextField(
//                        value = description,
//                        onValueChange = { description = it },
//                        label = { Text("توضیحات محصول", textAlign = TextAlign.End) },
//                        placeholder = {
//                            Text(
//                                "توضیحات محصول را وارد کنید",
//                                textAlign = TextAlign.End
//                            )
//                        },
//                        modifier = Modifier.fillMaxWidth(),
//                        textStyle = androidx.compose.ui.text.TextStyle(
//                            textAlign = TextAlign.End
//                        )
//                    )
//
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    OutlinedTextField(
//                        value = price,
//                        onValueChange = { price = it },
//                        label = { Text("قیمت محصول", textAlign = TextAlign.End) },
//                        placeholder = {
//                            Text(
//                                "قیمت محصول را وارد کنید",
//                                textAlign = TextAlign.End
//                            )
//                        },
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                        modifier = Modifier.fillMaxWidth(),
//                        textStyle = androidx.compose.ui.text.TextStyle(
//                            textAlign = TextAlign.End
//                        )
//                    )
//
//                    Spacer(modifier = Modifier.height(16.dp))
//
//
//                    AddProductImages(context = context, onImagesSelected = { selectedImages ->
//                        images = selectedImages
//                    })
//                }
//            }
//        }
//
//        Box(
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .padding(16.dp)
//        ) {
//            Box(
//                modifier = Modifier
//                    .align(Alignment.BottomCenter)
//                    .padding(16.dp)
//            ) {
//                Button(onClick = {
//                    if (name.isNotEmpty() && description.isNotEmpty() && price.isNotEmpty() && phone.isNotEmpty() && images.isNotEmpty()) {
//                        userViewModel.sendProduct(
//                            name = name,
//                            description = description,
//                            price = price,
//                            phone = phone,
//                            images = images,
//                            context = context
//                        )
//                    } else {
//                        Toast.makeText(context, "لطفاً تمام فیلدها را پر کنید", Toast.LENGTH_LONG)
//                            .show()
//                    }
//                }) {
//                    Text("ذخیره محصول")
//                }
//
//            }
//        }
//    }
//}


@Composable
fun AddProductScreen(navController: NavController) {
    val name = remember { mutableStateOf(TextFieldValue("")) }
    val description = remember { mutableStateOf(TextFieldValue("")) }
    val nameUser = remember { mutableStateOf(TextFieldValue("")) }
    val phone = remember { mutableStateOf(TextFieldValue("")) }
    val city = remember { mutableStateOf(TextFieldValue("")) }
    val address = remember { mutableStateOf(TextFieldValue("")) }
    val family = remember { mutableStateOf(TextFieldValue("")) }
    val price = remember { mutableStateOf(TextFieldValue("")) }
    var imageUri by remember { mutableStateOf<Uri?>(null) } // برای انتخاب تصویر
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(modifier = Modifier.padding(16.dp)) {
        BasicTextField(
            value = name.value,
            onValueChange = { name.value = it },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        // سایر فیلدها...

        Button(onClick = {
            scope.launch {
                try {
                    val contentResolver = context.contentResolver
                    val inputStream = contentResolver.openInputStream(imageUri!!)
                    val tempFile = File(context.cacheDir, "temp_image.jpg")
                    tempFile.outputStream().use { output ->
                        inputStream?.copyTo(output)
                    }

                    val requestBody = tempFile.asRequestBody("image/*".toMediaTypeOrNull())
                    val imagePart =
                        MultipartBody.Part.createFormData("image", tempFile.name, requestBody)

                    val response = UtilsRetrofit.api.addProduct(
                        name = RequestBody.create(
                            "text/plain".toMediaTypeOrNull(),
                            name.value.text
                        ),
                        description = RequestBody.create(
                            "text/plain".toMediaTypeOrNull(),
                            description.value.text
                        ),
                        nameUser = RequestBody.create(
                            "text/plain".toMediaTypeOrNull(),
                            nameUser.value.text
                        ),
                        phone = RequestBody.create(
                            "text/plain".toMediaTypeOrNull(),
                            phone.value.text
                        ),
                        city = RequestBody.create(
                            "text/plain".toMediaTypeOrNull(),
                            city.value.text
                        ),
                        address = RequestBody.create(
                            "text/plain".toMediaTypeOrNull(),
                            address.value.text
                        ),
                        family = RequestBody.create(
                            "text/plain".toMediaTypeOrNull(),
                            family.value.text
                        ),
                        price = RequestBody.create(
                            "text/plain".toMediaTypeOrNull(),
                            price.value.text
                        ),
                        image = imagePart
                    )

                    if (response.isSuccessful) {
                        Toast.makeText(context, "محصول با موفقیت اضافه شد.", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(context, "خطا در ارسال داده‌ها.", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "خطا: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }) {
            Text("ارسال")
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductImages(context: Context, onImagesSelected: (List<Uri>) -> Unit) {
    // لیستی برای ذخیره تصاویر
    var images by remember { mutableStateOf<List<Uri>>(emptyList()) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (uris != null && uris.size <= 10) {
            images = uris
            onImagesSelected(uris) // Pass the selected images back to the parent composable
        } else {
            Toast.makeText(
                context,
                "حداکثر 10 تصویر می‌توانید انتخاب کنید.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(images) { image ->
                Image(
                    painter = rememberImagePainter(image),
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { imagePicker.launch("image/*") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("انتخاب تصاویر")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("تعداد تصاویر انتخابی: ${images.size}/10")
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun showData() {
    val context = LocalContext.current
    val navController = rememberNavController()
    AddProductScreen(navController)
}


@Composable
fun favoritesScreen() {
    Text(text = "favorites Screen")
}
