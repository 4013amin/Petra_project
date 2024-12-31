import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.shop_app_project.Home_page.Main.Screen_Item.AddProductForm
import com.example.shop_app_project.Home_page.Main.Screen_Item.FavoritesScreen
import com.example.shop_app_project.Home_page.Main.Screen_Item.LoginUsers.addCodeScreen
import com.example.shop_app_project.Home_page.Main.Screen_Item.LoginUsers.forgetpasswordScreen
import com.example.shop_app_project.Home_page.Main.Screen_Item.ProductDetailScreen

import com.example.shop_app_project.Home_page.Main.UiHomePage
import com.example.shop_app_project.data.models.product.ProductModel
import com.example.shop_app_project.data.view_model.ShoppingCartViewModel
import com.example.shop_app_project.data.view_model.UserViewModel

data class NavigationsItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
)

val navItems = listOf(
    NavigationsItem("forgetPassword", "پروفایل", Icons.Default.Person),
    NavigationsItem("favorites", "نشان ها", Icons.Default.Favorite),
    NavigationsItem("addProduct", "ثبت آگهی", Icons.Default.Add),
    NavigationsItem("home", "فروشگاه", Icons.Default.Home),

    )

@Composable
fun BottomNavigationBar(
    navController: NavHostController
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            navItems.reversed().forEach { item ->  // Reversed to match RTL order
                val isSelected = currentDestination?.route == item.route
                val scale = animateFloatAsState(targetValue = if (isSelected) 1.2f else 1f).value

                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            modifier = Modifier
                                .size(24.dp)
                                .padding(4.dp)
                                .graphicsLayer(scaleX = scale, scaleY = scale)
                        )
                    },
                    label = {
                        AnimatedVisibility(visible = isSelected) {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = Color.Gray,
                        indicatorColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigations(
    navController: NavHostController,
    userViewModel: UserViewModel,
    shoppingCartViewModel: ShoppingCartViewModel,
) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        NavGraph(
            navController = navController,
            shoppingCartViewModel = shoppingCartViewModel,
            modifier = Modifier.padding(innerPadding),
            userViewModel
        )
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph(
    navController: NavHostController,
    shoppingCartViewModel: ShoppingCartViewModel,
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel
) {
    NavHost(navController = navController, startDestination = "home", modifier = modifier) {
        composable("home") {
            UiHomePage(navController = navController)
        }

        composable("singleProduct/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
            if (productId != null) {
                val context = LocalContext.current
                val product = remember { mutableStateOf<ProductModel?>(null) }

                LaunchedEffect(productId) {
                    product.value = userViewModel.getProductById(context, productId)
                }

                product.value?.let { productDetails ->
                    ProductDetailScreen(product = productDetails)
                } ?: Text("Loading...")
            } else {
                Text("Invalid product ID")
            }
        }


        composable("favorites") {
            val favoritesViewModel: FavoritesViewModel = viewModel()
            FavoritesScreen(navController = navController, favoritesViewModel = favoritesViewModel)
        }

        composable("forgetPassword") {
            forgetpasswordScreen(navController, userViewModel)
        }

        composable("addCodeScreen?phone={phone}") { backStackEntry ->
            val phone = backStackEntry.arguments?.getString("phone")
            addCodeScreen(navController, userViewModel, phone)
        }

        composable("addProduct") {
            AddProductForm(navController)
        }
    }
}


@Preview
@Composable
private fun showBottomNavigation() {

    val navController = rememberNavController()
    BottomNavigationBar(navController = navController)
}
