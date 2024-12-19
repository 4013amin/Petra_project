import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.shop_app_project.Home_page.Main.Screen_Item.AddProductForm
import com.example.shop_app_project.Home_page.Main.Screen_Item.LoginUsers.addCodeScreen
import com.example.shop_app_project.Home_page.Main.Screen_Item.LoginUsers.forgetpasswordScreen
import com.example.shop_app_project.Home_page.Main.Screen_Item.ProductDetailScreen
import com.example.shop_app_project.Home_page.Main.Screen_Item.favoritesScreen

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
    NavigationsItem("home", "فروشگاه", Icons.Default.Home),
    NavigationsItem("addProduct", "ثبت آگهی", Icons.Default.Add),
    NavigationsItem("favorites", "نشان ها", Icons.Default.Favorite),
    NavigationsItem("forgetPassword", "پروفایل", Icons.Default.Person),
)

@Composable
fun BottomNavigationBar(
    navController: NavHostController
) {
    NavigationBar {
        val currentBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStackEntry?.destination

        navItems.reversed().forEach { item ->
            NavigationBarItem(
                selected = currentDestination?.route == item.route,
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
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(text = item.title)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = Color.Gray,
                    indicatorColor = MaterialTheme.colorScheme.secondary
                )
            )
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
            favoritesScreen()
        }

        composable("forgetPassword") {
            forgetpasswordScreen(navController)
        }

        composable("addCode") {
            addCodeScreen(navController)
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
