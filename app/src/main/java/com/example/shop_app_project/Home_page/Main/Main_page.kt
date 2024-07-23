package com.example.shop_app_project.Home_page.Main

import BottomNavigations
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.shop_app_project.R
import com.example.shop_app_project.data.view_model.ShoppingCartViewModel
import com.example.shop_app_project.data.view_model.UserViewModel
import com.example.shop_app_project.ui.theme.Shop_App_projectTheme
import com.google.gson.Gson

val gson = Gson()

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Shop_App_projectTheme {
                val navController = rememberNavController()
                val userViewModel: UserViewModel = viewModel()
                val shoppingCartViewModel: ShoppingCartViewModel = viewModel()


//                SetupNavGraph(
//                    navController = navController,
//                    userViewModel = userViewModel,
//                    shoppingCartViewModel = shoppingCartViewModel
//                )

                UiHomePage(cartViewModel = shoppingCartViewModel, navController = navController)
                BottomNavigations(navController, userViewModel, shoppingCartViewModel)
            }
        }
    }
}

data class CategoryModel(
    val name: String,
    val imageRes: Int,
)

data class ProductModel(
    val name: String,
    val description: String,
    val price: Int,
    val image: Int,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UiHomePage(
    userViewModel: UserViewModel = viewModel(),
    cartViewModel: ShoppingCartViewModel,
    navController: NavController,
) {

    val cartItems by cartViewModel.cartItems.collectAsState()


    val categories = listOf(
        CategoryModel("Cat", R.drawable.cat),
        CategoryModel("Dog", R.drawable.dog),
        CategoryModel("Test", R.drawable.logo),
        // add more categories as needed
    )

    val products = listOf(
        ProductModel("Dog Food", "High-quality dog food", 50, R.drawable.tools),
        ProductModel("Cat Toy", "Fun toy for cats", 20, R.drawable.cat_image),
        ProductModel("Bird Cage", "Spacious bird cage", 150, R.drawable.tools),
        ProductModel("Fish Tank", "Large fish tank", 100, R.drawable.cat_image),
        ProductModel("Rabbit Hutch", "Comfortable hutch for rabbits", 120, R.drawable.tools)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Pet Store") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("search")
                    }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Explore")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("cart")
                    }) {
                        BadgedBox(badge = {
                            val cartItems by cartViewModel.cartItems.collectAsState()
                            if (cartItems.isNotEmpty()) {
                                Badge(
                                    containerColor = Color.Transparent,
                                    contentColor = Color(0xFF0ED918),
                                    modifier = Modifier.size(16.dp)
                                ) {
                                    Text(text = cartItems.size.toString())
                                }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Cart"
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {

            // Image Slider
            item {
                ImageSlider(
                    images = listOf(
                        R.drawable.image_slider
                    )
                )
            }

            item {
                Spacer(modifier = Modifier.height(25.dp))
            }


            item {
                Text(
                    text = "Trending now",
                    fontSize = 30.sp,
                    color = Color(0xFFCD8822),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // TrendProduct
            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    items(products) { product ->
                        ProductItem(
                            name = product.name,
                            description = product.description,
                            price = product.price,
                            image = product.image,
                            addToCart = {
                                cartViewModel.addToCart(product)
                            },
                            onClick = {

                                navController.navigate("singlePage")
                            }
                        )
                    }
                }
            }


            //CatProduct
            item {
                Text(
                    text = "Browse pet types",
                    fontSize = 30.sp,
                    color = Color(0xFFCD8822),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Animal Boxes
            item {
                AnimalBoxes()
            }

            item {
                Spacer(modifier = Modifier.height(18.dp))
            }


            item {
                Text(
                    text = "Products For you",
                    fontSize = 30.sp,
                    color = Color(0xFFCD8822),
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
                            addToCart = {
                            },
                            onClick = {
                                val productJson = gson.toJson(product)
                                navController.navigate("ProductDetailsPage?product=$productJson")
                            }
                        )
                    }
                }
            }


            // Categories
            item {
                LazyRow(
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    items(categories) { category ->
                        CategoryItem(imageRes = category.imageRes, name = category.name)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(18.dp))
            }


            //DogProduct
            item {
                Text(
                    text = "Product For Dog",
                    fontSize = 30.sp,
                    color = Color(0xFFCD8822),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            item {
                LazyRow(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                ) {
                    items(products) { product ->
                        ProductItem(
                            name = product.name,
                            description = product.description,
                            price = product.price,
                            image = product.image,
                            addToCart = { /*TODO*/ }) {

                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ImageSlider(images: List<Int>) {
    LazyRow(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .height(200.dp) // ارتفاع مستطیل‌ها
    ) {
        items(images) { imageRes ->
            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxHeight() // به جای fillMaxSize از fillMaxHeight استفاده کنید
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxHeight() // به جای fillMaxSize از fillMaxHeight استفاده کنید
                        .aspectRatio(16f / 9f) // حفظ نسبت تصویر
                        .padding(horizontal = 8.dp)
                )
            }
        }
    }
}


//
//
//            item {
//                LazyRow(
//                    modifier = Modifier.padding(vertical = 8.dp)
//                ) {
//                    items(products) { product ->
//                        ProductItem(
//                            name = product.name,
//                            description = product.description,
//                            price = product.price,
//                            image = product.image,
//                            addToCart = {
////                                cartViewModel.addToCart(product)
//                            },
//                            onClick = {
//                                val productJson = gson.toJson(product)
//                                navController.navigate("single_product?product=$productJson")
//                            }
//                        )
//                    }
//                }
//            }
//        }
//    }
//}


@Composable
fun CategoryItem(imageRes: Int, name: String) {
    val isPressed = remember { mutableStateOf(false) }
    val backgroundColor =
        if (isPressed.value) Color.Green else Color(0xFFA5D6A7)

    Box(
        modifier = Modifier
            .padding(8.dp)
            .width(150.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(8.dp))
            .height(100.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        isPressed.value = true
                    },
                    onPress = {
                        tryAwaitRelease()
                        isPressed.value = false
                    }
                )
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = name,
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ProductItem(
    name: String,
    description: String,
    price: Int,
    image: Int, // استفاده از Int برای منابع محلی
    addToCart: () -> Unit,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .width(180.dp)
            .padding(8.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(8.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = null,
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = description,
            fontSize = 12.sp,
            color = Color.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = "$$price",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF388E3C)
        )
        Spacer(modifier = Modifier.height(4.dp))
        IconButton(
            onClick = addToCart,
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "",
                tint = Color.Black
            )
        }
    }
}

//AnimalsBox
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
                text = "parrot"
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
private fun Animals() {
    AnimalBoxes()
}

