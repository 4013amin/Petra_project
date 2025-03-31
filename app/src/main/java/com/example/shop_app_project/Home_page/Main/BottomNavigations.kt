import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.example.shop_app_project.Home_page.Main.ChatScreen.ChatScreen
import com.example.shop_app_project.Home_page.Main.ChatScreen.ChatUsersScreen
import com.example.shop_app_project.Home_page.Main.Screen_Item.AddProductForm
import com.example.shop_app_project.Home_page.Main.Screen_Item.EditProfileScreen
import com.example.shop_app_project.Home_page.Main.Screen_Item.FavoritesPage
import com.example.shop_app_project.Home_page.Main.Screen_Item.LoginUsers.addCodeScreen
import com.example.shop_app_project.Home_page.Main.Screen_Item.LoginUsers.forgetpasswordScreen
import com.example.shop_app_project.Home_page.Main.Screen_Item.ProductDetailScreen
import com.example.shop_app_project.Home_page.Main.Screen_Item.UserProductsScreen
import com.example.shop_app_project.Home_page.Main.Screen_Item.UserProfileDetailScreen
import com.example.shop_app_project.Home_page.Main.SharedPreferencesManager.SharedPreferencesManager

import com.example.shop_app_project.Home_page.Main.UiHomePage
import com.example.shop_app_project.data.models.Profile.UserProfile
import com.example.shop_app_project.data.models.product.ProductModel
import com.example.shop_app_project.data.view_model.SavedProductsViewModel
import com.example.shop_app_project.data.view_model.ShoppingCartViewModel
import com.example.shop_app_project.data.view_model.UserViewModel

data class NavigationsItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
)

val navItems = listOf(
    NavigationsItem("profile", "پروفایل", Icons.Default.Person),
    NavigationsItem("chat_users", "پیام ها", Icons.Default.ThumbUp),
    NavigationsItem("favorites", "نشان ها", Icons.Default.FavoriteBorder),
    NavigationsItem("addProduct", "ثبت آگهی", Icons.Default.Add),
    NavigationsItem("home", "خانه", Icons.Default.Home),
)

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    userProfile: UserProfile?,
    userPhone: String // Add userPhone parameter
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            NavigationBar(
                containerColor = Color.Transparent,
                tonalElevation = 0.dp,
            ) {
                navItems.reversed().forEach { item ->
                    val isSelected = currentDestination?.route?.startsWith(item.route) == true
                    val scale =
                        animateFloatAsState(targetValue = if (isSelected) 1.2f else 1f).value

                    NavigationBarItem(
                        selected = isSelected,
                        modifier = Modifier.height(15.dp),
                        onClick = {
                            val route = if (item.route == "chat_users") {
                                "chat_users/$userPhone" // Use phone for chat_users route
                            } else {
                                item.route
                            }
                            navController.navigate(route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            if (item.route == "profile" && userProfile?.image != null) {
                                val imageUrl = if (userProfile.image.startsWith("http")) {
                                    userProfile.image
                                } else {
                                    "http://192.168.1.110:2020${userProfile.image}"
                                }
                                AsyncImage(
                                    model = imageUrl,
                                    contentDescription = null,
                                    contentScale = ContentScale.Inside,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                )
                            } else {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .graphicsLayer(scaleX = scale, scaleY = scale),
                                    tint = if (isSelected) Color(0xFF007BFF) else Color.Gray
                                )
                            }
                        },
                        label = {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (isSelected) Color.Black else Color.Gray
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            unselectedIconColor = Color.Gray,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottomNavigations(
    navController: NavHostController,
    userViewModel: UserViewModel,
    shoppingCartViewModel: ShoppingCartViewModel,
    userProfile: UserProfile?
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val isBottomBarVisible = remember { mutableStateOf(true) }
    val scrollState = rememberLazyListState()
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences.getInstance(context) }
    val userPhone = userPreferences.getUserPhone() ?: "09052278519"

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (available.y > 0) {
                    isBottomBarVisible.value = true
                } else if (available.y < 0) {
                    isBottomBarVisible.value = false
                }
                return Offset.Zero
            }
        }
    }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = isBottomBarVisible.value && currentRoute != "forgetPassword" && currentRoute?.startsWith(
                    "addCodeScreen"
                ) != true
            ) {
                BottomNavigationBar(
                    navController = navController,
                    userProfile = userProfile,
                    userPhone = userPhone
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .nestedScroll(nestedScrollConnection)
        ) {
            NavGraph(
                navController = navController,
                shoppingCartViewModel = shoppingCartViewModel,
                modifier = Modifier,
                userViewModel = userViewModel
            )
        }
    }
}

@SuppressLint("UnrememberedMutableState", "NewApi")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph(
    navController: NavHostController,
    shoppingCartViewModel: ShoppingCartViewModel,
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel
) {
    val context = LocalContext.current
    val userPreferences = remember {
        UserPreferences.getInstance(context)
    }

    val isUserLoggedIn = remember {
        mutableStateOf(userPreferences.isUserLoggedIn())
    }

    NavHost(
        navController = navController,
        startDestination = if (isUserLoggedIn.value) "home" else "forgetPassword",
        modifier = modifier
    ) {
        composable("home") {
            UiHomePage(navController = navController, userViewModel = userViewModel)
        }

        composable("singleProduct/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
            if (productId != null) {
                val context = LocalContext.current
                val product = remember { mutableStateOf<ProductModel?>(null) }
                val isLoading = remember { mutableStateOf(true) }
                val errorMessage = remember { mutableStateOf<String?>(null) }

                LaunchedEffect(productId) {
                    try {
                        isLoading.value = true
                        errorMessage.value = null
                        product.value = userViewModel.getProductById(context, productId)
                        if (product.value == null) {
                            errorMessage.value = "محصول پیدا نشد."
                        }
                    } catch (e: Exception) {
                        errorMessage.value = "خطا در دریافت اطلاعات محصول."
                    } finally {
                        isLoading.value = false
                    }
                }

                when {
                    isLoading.value -> {
                        // نمایش حالت بارگذاری
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    errorMessage.value != null -> {
                        // نمایش پیام خطا
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = errorMessage.value ?: "خطای ناشناخته",
                                color = Color.Red,
                                fontSize = 18.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    product.value != null -> {
                        // نمایش جزئیات محصول
                        ProductDetailScreen(
                            product = product.value!!,
                            onBackClick = { navController.popBackStack() },
                            navController = navController
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "شناسه محصول نامعتبر است.",
                        color = Color.Red,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }


        composable("favorites") {
            val favoritesViewModel: SavedProductsViewModel = viewModel()
            FavoritesPage(favoritesViewModel, navController)
        }


        composable("forgetPassword") {
            forgetpasswordScreen(navController)
        }


        composable("addCodeScreen?phone={phone}") { backStackEntry ->
            val phone = backStackEntry.arguments?.getString("phone")
            addCodeScreen(navController, userViewModel, phone)
        }


        composable("addProduct") {
            AddProductForm(navController)
        }

        composable("profile") {
            val userPreferences = UserPreferences.getInstance(context)
            val phone = userPreferences.getUserPhone()
            UserProductsScreen(userViewModel, phone.toString(), context, navController)
        }

        composable("profileDetail") {
            UserProfileDetailScreen(userViewModel, navController)
        }

        composable("EditProfileScreen") {
            EditProfileScreen(userViewModel, navController, context)
        }



        composable(
            "chat_users/{phone}",
            arguments = listOf(navArgument("phone") { type = NavType.StringType })
        ) { backStackEntry ->
            val phone = backStackEntry.arguments?.getString("phone") ?: ""
            ChatUsersScreen(navController, phone)
        }

        composable(
            "chat/{phone}/{receiver}",
            arguments = listOf(
                navArgument("phone") { type = NavType.StringType },
                navArgument("receiver") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val phone = backStackEntry.arguments?.getString("phone") ?: ""
            val receiver = backStackEntry.arguments?.getString("receiver") ?: ""
            ChatScreen(navController, phone, receiver)
        }


    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewBottomNavigationBar() {
//    val navController = rememberNavController()
//
//    // داده آزمایشی برای userProfile
//    val sampleUserProfile =
//        UserProfile(1, "username", image = "https://via.placeholder.com/150")
//
//    BottomNavigationBar(navController = navController, userProfile = sampleUserProfile)
//
//}
