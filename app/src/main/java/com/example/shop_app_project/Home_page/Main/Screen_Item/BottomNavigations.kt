import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.shop_app_project.Home_page.Main.Screen_Item.ProductDetailsPage
import com.example.shop_app_project.Home_page.Main.Screen_Item.SearchPage
import com.example.shop_app_project.Home_page.Main.UiHomePage
import com.example.shop_app_project.R
import com.example.shop_app_project.data.view_model.ShoppingCartViewModel
import com.example.shop_app_project.data.view_model.UserViewModel

data class NavigationsItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
)

val navItems = listOf(
    NavigationsItem("home", "Shop", Icons.Default.Home),
    NavigationsItem("search", "Explore", Icons.Default.Search),
    NavigationsItem("cart", "Cart", Icons.Default.ShoppingCart),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigations(
    navController: NavHostController,
    userViewModel: UserViewModel,
    shoppingCartViewModel: ShoppingCartViewModel,
) {
    Scaffold(
        bottomBar = {
            Box(modifier = Modifier.fillMaxWidth()) {
                NavigationBar(
                    containerColor = Color(0xFFF8F8F8),
                    contentColor = Color.White,
                    modifier = Modifier.align(Alignment.BottomCenter),
                    tonalElevation = 8.dp
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route
//                    val cartItems by shoppingCartViewModel.cartItems.collectAsState()

//                    navItems.filter { it.route != "search" }.forEach { item ->
//                        NavigationBarItem(
//                            icon = {
//                                Box {
//                                    if (item.route == "cart") {
//                                        BadgedBox(badge = {
//                                            if (cartItems.isNotEmpty()) {
//                                                Badge {
//                                                    Text(text = cartItems.size.toString())
//                                                }
//                                            }
//                                        }) {
//                                            Icon(
//                                                imageVector = item.icon,
//                                                contentDescription = item.title,
//                                                modifier = Modifier.size(28.dp)
//                                            )
//                                        }
//                                    } else {
//                                        Icon(
//                                            imageVector = item.icon,
//                                            contentDescription = item.title,
//                                            modifier = Modifier.size(24.dp)
//                                        )
//                                    }
//                                }
//                            },
//                            label = { Text(item.title) },
//                            selected = currentRoute == item.route,
//                            onClick = {
//                                navController.navigate(item.route) {
//                                    popUpTo(navController.graph.startDestinationId) {
//                                        saveState = true
//                                    }
//                                    launchSingleTop = true
//                                    restoreState = true
//                                }
//                            },
//                            colors = NavigationBarItemDefaults.colors(
//                                selectedIconColor = Color(0xFFFE5B52),
//                                unselectedIconColor = Color.Gray,
//                                selectedTextColor = Color(0xFFFE5B52),
//                                unselectedTextColor = Color.Gray
//                            )
//                        )
//                    }
                }

                FloatingActionButton(
                    onClick = {
                        navController.navigate("search") {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    shape = CircleShape,
                    containerColor = Color(0xFFFE5B52),
                    contentColor = Color.White,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = (-30).dp)
                        .size(60.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Paw Button",
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        NavGraph(
            navController = navController,
            shoppingCartViewModel = shoppingCartViewModel,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph(
    navController: NavHostController,
    shoppingCartViewModel: ShoppingCartViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = "home", modifier = modifier) {
        composable("home") {
            UiHomePage(cartViewModel = shoppingCartViewModel, navController = navController)
        }
        composable("search") {
            SearchPage(navController = navController, shoppingCartViewModel)
        }
        composable("singleProduct") {
            ProductDetailsPage(navController , shoppingCartViewModel)
        }

    }
}