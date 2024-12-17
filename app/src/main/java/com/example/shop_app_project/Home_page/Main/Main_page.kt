package com.example.shop_app_project.Home_page.Main

import BottomNavigations
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.shop_app_project.R
import com.example.shop_app_project.data.view_model.ShoppingCartViewModel
import com.example.shop_app_project.data.view_model.UserViewModel
import com.example.shop_app_project.ui.theme.Shop_App_projectTheme
import com.google.gson.Gson
import com.google.accompanist.pager.*

val gson = Gson()

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            Shop_App_projectTheme {
                val navController = rememberNavController()
                val userViewModel: UserViewModel = viewModel()
                val shoppingCartViewModel: ShoppingCartViewModel = viewModel()

                UiHomePage(cartViewModel = shoppingCartViewModel, navController = navController)

                BottomNavigations(navController, userViewModel, shoppingCartViewModel)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UiHomePage(
    userViewModel: UserViewModel = viewModel(),
    cartViewModel: ShoppingCartViewModel,
    navController: NavController,
) {
//    val cartItems by cartViewModel.cartItems.collectAsState()
    val products by userViewModel.products
    val category by userViewModel.category


    LaunchedEffect(key1 = true) {
        userViewModel.getAllProducts()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Pet Store", color = Color.Black) },

                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color(0xFFFEA500))
            )
        },

        ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {

            // Image Slider
            item {
                ImageSlider(
                    images = listOf(
                        R.drawable.slaider1,
                        R.drawable.slider2,
                        R.drawable.slider3
                    )
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            //Categories
            item {
                Text(
                    text = "Categories",
                    fontSize = 18.sp,
                    color = Color(0xFF2E1F09),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    items(category) { category ->
                        CategoryItem(
                            created_at = category.created_at,
                            name = category.name
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Trending Products

            item {
                Text(
                    text = "Trending Now",
                    fontSize = 18.sp,
                    color = Color(0xFF2E1F09),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    items(products) { product ->
                        ProductItem(
                            name = product.name,
                            description = product.description,
                            price = product.price,
                            image = product.image,

                            onClick = {
                                navController.navigate("singleProduct")
                            }
                        )
                    }
                }
            }


            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Text(
                    text = "Products For You",
                    fontSize = 18.sp,
                    color = Color(0xFF2E1F09),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    items(products) { product ->
                        ProductItem(
                            name = product.name,
                            description = product.description,
                            price = product.price,
                            image = product.image,
                            onClick = {
                                val productJson = gson.toJson(product)
                                navController.navigate("singleProduct")
                            }
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Text(
                    text = "Products for Dog",
                    fontSize = 18.sp,
                    color = Color(0xFF2E1F09),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    items(products) { product ->
                        ProductItem(
                            name = product.name,
                            description = product.description,
                            price = product.price,
                            image = product.image,
                            onClick = {
                                navController.navigate("singleProduct")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ImageSlider(images: List<Int>) {
    val pagerState = rememberPagerState()

    Column {
        HorizontalPager(
            count = images.size,
            state = pagerState,
            modifier = Modifier.height(200.dp)
        ) { page ->
            Image(
                painter = painterResource(id = images[page]),
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
    pagerState: PagerState,
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


@Composable
fun CategoryItem(created_at: String, name: String) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
            .clickable { /* Handle click event */ }
            .padding(16.dp)
            .width(120.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = created_at,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = name,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ProductItem(
    name: String,
    description: String,
    price: Int,
    image: String,
    onClick: () -> Unit,
) {
    var isSelected by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .width(260.dp)
            .height(280.dp)
            .padding(8.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(
                width = 2.dp, // ضخامت بردر
                color = Color(0xFFFFA500), // رنگ نارنجی برای بردر
                shape = RoundedCornerShape(8.dp) // شکل گوشه‌های بردر
            )
            .clickable(onClick = onClick)
    ) {

        AsyncImage(
            model = image,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )


        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(8.dp)
                .fillMaxWidth()
        ) {

            Text(
                text = name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFCC9C99) // رنگ صورتی برای متن
            )

            Text(
                text = description,
                fontSize = 12.sp,
                color = Color(0xFFCC9C99), // رنگ صورتی برای متن
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "$$price",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF388E3C),
                )

            }
        }
    }
}


@Composable
fun AnimalBox(imageRes: Int, backgroundColor: Color, text: String) {
    Box(
        modifier = Modifier
            .size(width = 150.dp, height = 100.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(16.dp))
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = text,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun AnimalBoxes() {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            AnimalBox(
                imageRes = R.drawable.dog,
                backgroundColor = Color(0xFFFFF3E0),
                text = "Dog"
            )
        }
        item {
            AnimalBox(
                imageRes = R.drawable.cat,
                backgroundColor = Color(0xFFE0F7FA),
                text = "Cat"
            )
        }
        item {
            AnimalBox(
                imageRes = R.drawable.parrot,
                backgroundColor = Color(0xFFFFF3E0),
                text = "Parrot"
            )
        }
        item {
            AnimalBox(
                imageRes = R.drawable.tools3,
                backgroundColor = Color(0xFFE0F7FA),
                text = "Tools"
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewUiHomePage() {
    Shop_App_projectTheme {
        UiHomePage(
            userViewModel = viewModel(),
            cartViewModel = viewModel(),
            navController = rememberNavController()
        )
    }
}
