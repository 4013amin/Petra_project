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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.shop_app_project.Home_page.Main.Screen_Item.ProductDetailScreen

import com.example.shop_app_project.Home_page.Main.UiHomePage
import com.example.shop_app_project.R
import com.example.shop_app_project.data.models.product.PorductModel
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
    ) { innerPadding ->
        NavGraph(
            navController = navController,
            shoppingCartViewModel = shoppingCartViewModel,
            modifier = Modifier.padding(innerPadding),
            userViewModel
        )
    }
}

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
                val product = remember { mutableStateOf<PorductModel?>(null) }

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


    }
}



