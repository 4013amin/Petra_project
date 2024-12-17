package com.example.shop_app_project.Home_page.Main.Screen_Item

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.shop_app_project.R
import com.example.shop_app_project.data.view_model.ShoppingCartViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch
import androidx.compose.material3.*
import com.example.shop_app_project.data.models.product.PorductModel


var gson = Gson()


data class CategoryModel(
    val name: String,
    val imageRes: Int,
)

data class prductmodelfack(
    val name: String,
    val des: String,
    val price: Int,
    val image: Int
)


@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchPage(navController: NavController, shoppingCartViewModel: ShoppingCartViewModel) {
    Scaffold(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // SearchBar placed at the top
                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                // Content below the SearchBar
//                CategoryFilter()
                Spacer(modifier = Modifier.height(16.dp))
//                ProductGrid(shoppingCartViewModel, navController)
            }
        }
    )
}

@ExperimentalMaterial3Api
@Composable
fun SearchBar(modifier: Modifier) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    TextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = { Text(text = "Search Product or Brand") },
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(8.dp)),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon"
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}


//@Composable
//fun ProductGrid(
//    cartViewModel: ShoppingCartViewModel,
//    navController: NavController
//) {
//    val products by U
//    // Create a grid layout with 2 columns
//    LazyVerticalGrid(
//        columns = GridCells.Fixed(2),
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(10.dp),
//        horizontalArrangement = Arrangement.spacedBy(8.dp),
//        verticalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        items(products) { product ->
//            ProductItemSearchPage(
//                name = product.name,
//                description = product.description,
//                price = product.price,
//                image = product.image,
//                addToCart = {
//                    // Add to cart functionality
//                },
//                onClick = {
//                    navController.navigate("singleProduct")
//                }
//            )
//        }
//    }
//}


@Composable
fun ProductItemSearchPage(
    name: String,
    description: String,
    price: Int,
    image: Int,
    addToCart: () -> Unit,
    onClick: () -> Unit
) {
    var isSelected by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .width(300.dp)
            .height(260.dp)
            .padding(8.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFFFFA500), RoundedCornerShape(8.dp)) // Orange border
            .clickable(onClick = onClick)
    ) {

        Box {
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            // Discount banner
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(Color(0xFFFFA500), RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Discount : ${price}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }

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
                color = Color(0xFFCC9C99)
            )

            Text(
                text = description,
                fontSize = 12.sp,
                color = Color(0xFFCC9C99),
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
                    color = Color(0xFF388E3C)
                )

                IconButton(
                    onClick = addToCart,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add to cart",
                        tint = Color(0xFFFFA500)
                    )
                }
            }
        }
    }
}


////cardPage
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CartPage(cartViewModel: ShoppingCartViewModel, navController: NavController) {
//    val cartItems by cartViewModel.cartItems.collectAsState()
//    val scope = rememberCoroutineScope()
//
//    var showBottomSheet by remember { mutableStateOf(false) }
//    val sheetState = rememberModalBottomSheetState()
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Shopping Cart") },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
//                    }
//                }
//            )
//        },
//
//        ) { paddingValues ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//        ) {
//            if (cartItems.isEmpty()) {
//                EmptyCartAnimation()
//            } else {
//                LazyColumn(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(bottom = 80.dp)
//                ) {
//                    items(cartItems) { product ->
//                        CartItem(
//                            product = product,
//                            onRemove = { cartViewModel.removeFromCart(product) },
//                            onClick = {
//                                selectedProduct = product
//                                showBottomSheet = true
//                            }
//                        )
//                    }
//                }
//
//                Column(
//                    modifier = Modifier
//                        .align(Alignment.BottomCenter)
//                        .padding(16.dp)
//                ) {
//
//                    Button(
//                        onClick = { cartViewModel.clearCart() },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .background(Color(0xFFFFB004)),
//                        colors = ButtonDefaults.outlinedButtonColors(
//                            containerColor = Color(0xFFFFB004),
//                            contentColor = Color.White
//                        )
//                    ) {
//                        Text(text = "Clear Cart")
//                    }
//
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    Button(
//                        onClick = { },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .background(Color(0xFFFFB004)),
//                        colors = ButtonDefaults.outlinedButtonColors(
//                            containerColor = Color(0xFFFFB004),
//                            contentColor = Color.White
//                        )
//                    ) {
//                        Text(text = "payment")
//                    }
//
//                }
//            }
//        }
//
//        if (showBottomSheet) {
//            ModalBottomSheet(
//                onDismissRequest = { showBottomSheet = false },
//                sheetState = sheetState
//            ) {
//                selectedProduct?.let { product ->
//                    ProductDetailsBottomSheet(product = product, onClose = {
//                        scope.launch { sheetState.hide() }.invokeOnCompletion {
//                            if (!sheetState.isVisible) {
//                                showBottomSheet = false
//                            }
//                        }
//                    })
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun CartItem(
//    product: com.example.shop_app_project.Home_page.Main.ProductModel,
//    onRemove: () -> Unit,
//    onClick: () -> Unit
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp)  // Padding outside the box for spacing between items
//            .clip(RoundedCornerShape(8.dp))  // Clip to rounded corners
//            .background(Color.White)  // Set background color to white
//            .padding(15.dp)  // Padding inside the box
//            .clickable { onClick() }  // Handle item click
//    ) {
//
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Image(
//                painter = rememberImagePainter(data = product.image),
//                contentDescription = null,
//                modifier = Modifier
//                    .size(64.dp)
//                    .clip(CircleShape),  // Make image circular
//                contentScale = ContentScale.Crop
//            )
//
//            Spacer(modifier = Modifier.width(16.dp))
//
//            Column(
//                modifier = Modifier.weight(1f)
//            ) {
//                Text(
//                    text = product.name,
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color(0xFFFFB004)
//                )
//                // Add more text fields if needed for other details
//            }
//
//            Spacer(modifier = Modifier.width(16.dp))
//
//            Button(
//                onClick = onRemove,
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFFE0F7FA),
//                    contentColor = Color.Black
//                )
//            ) {
//                Text(text = "Remove", color = Color.Black)
//            }
//        }
//    }
//}

@Composable
fun ProductDetailsBottomSheet(
    product: PorductModel,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = product.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = product.description,
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "$${product.price}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onClose,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE0F7FA),
                contentColor = Color.Black
            )
        ) {
            Text(text = "Close", color = Color.Black)
        }
    }
}


@Composable
fun EmptyCartAnimation() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .clip(RoundedCornerShape(0.dp)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.emptycat),
            contentDescription = "Empty Cart",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}


@Composable
fun ProfilePage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Profile Page", fontSize = 32.sp)
    }
}


@Composable
fun ProductDetailsPage(navController: NavController, cartViewModel: ShoppingCartViewModel) {
    var counter by remember { mutableStateOf(1) }
    var scorller = rememberScrollState()
    val products = ProductModel(
        name = "Symply Dog Adult Chicken With Rice & Vegetables",
        description = "High-quality dog food with chicken, rice, and vegetables.",
        price = 199,
        image = R.drawable.dog,
        additionalImages = listOf(
            R.drawable.shoptools2,
            R.drawable.shoptools,
            R.drawable.shoptools2,
            R.drawable.shoptools,
            R.drawable.shoptools2
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scorller)
                .padding(bottom = 80.dp)
        ) {

            Box(modifier = Modifier.fillMaxWidth()) {
                val pagerState =
                    rememberPagerState(pageCount = { products.additionalImages.size })

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                ) { page ->
                    Image(
                        painter = painterResource(id = products.additionalImages[page]),
                        contentDescription = "Product Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(MaterialTheme.shapes.medium),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color.White, shape = CircleShape)
                        .align(Alignment.TopStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            val pagerState = rememberPagerState(pageCount = { products.additionalImages.size })

            DotPagerIndicator(
                pagerState = pagerState,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                activeColor = Color.Black,
                inactiveColor = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(15.dp)
            ) {

                Text(
                    text = products.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = products.description,
                    fontSize = 16.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "£${products.price}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    IconButton(onClick = { if (counter > 1) counter -= 1 }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Decrease quantity",
                            tint = Color(0xFFD20311)
                        )
                    }
                    Text(
                        text = "$counter",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    IconButton(onClick = { counter += 1 }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase quantity",
                            tint = Color(0xFF00BF07)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        onClick = { },
                        modifier = Modifier
                            .padding(16.dp)
                            .clip(CircleShape)
                            .background(color = Color(0xFFFE5B52))
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun DotPagerIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    activeColor: Color,
    inactiveColor: Color
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pagerState.pageCount) { index ->
            val color = if (pagerState.currentPage == index) activeColor else inactiveColor
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color, shape = RoundedCornerShape(50))
                    .padding(2.dp)
            )
        }
    }
}

data class ProductModel(
    val name: String,
    val description: String,
    val price: Int,
    val image: Int,
    val additionalImages: List<Int>,
)

