package com.example.shop_app_project.Home_page.Main.Screen_Item

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.example.shop_app_project.data.models.product.ProductModel

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
                .padding(10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.End
        ) {
            // Image slider with top spacing
            Spacer(modifier = Modifier.height(20.dp))

            AsyncImage(
                model = "http://192.168.1.110:2020/${images}",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(420.dp)
                    .height(400.dp)
                    .padding(top = 36.dp)
                    .clip(RoundedCornerShape(8.dp)),

                onError = {
                    Log.e("ProductItem", "Error loading image: ${it.result.throwable?.message}")
                },
                onSuccess = {
                    Log.d("ProductItem", "Image loaded successfully")
                }
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "نام محصول : ${product.name}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))


            Text(
                text = "نام فروشنده : ${product.nameUser}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))


            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "منطقه :${product.city}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))


            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "نوع :${product.family}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Price: ${product.price} تومان",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Green
            )
            Spacer(modifier = Modifier.height(8.dp))


        }
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


