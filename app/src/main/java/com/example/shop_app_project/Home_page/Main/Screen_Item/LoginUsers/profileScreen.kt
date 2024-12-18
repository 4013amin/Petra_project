package com.example.shop_app_project.Home_page.Main.Screen_Item.LoginUsers

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.shop_app_project.R
import com.example.shop_app_project.data.view_model.UserViewModel


@SuppressLint("RememberReturnType")
@Composable
fun profileScreen(navController: NavController) {
    val context = LocalContext.current
    val phone = remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val scroller = rememberScrollState()

//    if (UserPreferences.isUserLoggedIn(context)) {
//        navController.navigate("mainScreen") {
//            popUpTo("register") { inclusive = true }
//        }
//        return
//    }


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.rectangle),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Image(
            painter = painterResource(id = R.drawable.patternloginr),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.TopEnd)
        )

        Image(
            painter = painterResource(id = R.drawable.patternloginl),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.TopStart)
                .offset(x = 0.dp, y = 32.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 150.dp)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.White)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.iconlogintop),
                        contentDescription = null,
                        modifier = Modifier.size(123.dp)
                    )

                    Text(
                        text = "ورود / ثبت نام",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "شماره تماس و رمز عبور خود را وارد کنید",
                        fontSize = 14.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(modifier = Modifier.padding(top = 35.dp)) {

                        var passwordVisible by remember { mutableStateOf(false) }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .padding(start = 16.dp, end = 16.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(text = "موبایل", textAlign = TextAlign.Right)
                            Spacer(modifier = Modifier.width(4.dp))
                        }

                        OutlinedTextField(
                            value = phone.value,
                            onValueChange = { phone.value = it },
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .padding(top = 8.dp),
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Right),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .padding(start = 16.dp, end = 16.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(text = "رمز عبور", textAlign = TextAlign.Right)
                            Spacer(modifier = Modifier.width(4.dp))
                        }

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .padding(top = 8.dp),
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Right),
                            leadingIcon = {
                                val icon = painterResource(id = R.drawable.baseline_password_24)
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        painter = icon,
                                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                                    )
                                }
                            }
                        )
                    }
                    AcceptTermsScreen(navController, phone.value, password)
                }
            }
        }
    }
}


@Composable
fun AcceptTermsScreen(navController: NavController, phone: String, password: String) {
    var accepted by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var showError by remember { mutableStateOf(false) }
    val userViewModel: UserViewModel = viewModel()

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "قوانین و شرایط را می‌پذیرم",
                color = colorResource(id = R.color.blueM),
                textAlign = TextAlign.End,
                modifier = Modifier.clickable { showDialog = true }
            )

            Checkbox(
                checked = accepted,
                onCheckedChange = { accepted = it }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (accepted) {
            Button(
                onClick = {
                    if (phone.isEmpty() || password.isEmpty()) {
                        showError = true
                    } else {
                        UserPreferences.saveUser(context, phone, password)
                        userViewModel.registerUser(phone, password)
                        navController.navigate("home")
                        {
                            popUpTo("register") { inclusive = true }
                        }
                    }
                },
                modifier = Modifier
                    .width(250.dp)
                    .height(100.dp)
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.blueM))
            ) {
                Text(
                    text = "ثبت نام",
                    color = Color.White,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }

            val annotatedString = buildAnnotatedString {
                append("حساب کاربری دارید ؟ ")
                pushStringAnnotation(tag = "login", annotation = "login")
                withStyle(style = SpanStyle(color = colorResource(id = R.color.blueM))) {
                    append("ورود به حساب  ")
                }
                pop()
            }

            ClickableText(
                text = annotatedString,
                onClick = { offset ->
                    annotatedString.getStringAnnotations("login", offset, offset)
                        .firstOrNull()?.let {
                            navController.navigate("login") {
                                popUpTo("register") { inclusive = true }
                            }
                        }
                },
                modifier = Modifier.padding(top = 16.dp),
            )
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = "قوانین و شرایط") },
                text = {
                    Column {
                        Text("1. این اپلیکیشن به شما این امکان را می‌دهد که به سادگی با دیگران ارتباط برقرار کنید.")
                        Text("2. اطلاعات شما محفوظ خواهد بود و به هیچ شخص ثالثی داده نخواهد شد.")
                        Text("3. استفاده از این اپلیکیشن باید مطابق با قوانین کشور باشد.")
                        Text("4. هرگونه سوءاستفاده از اپلیکیشن موجب مسدود شدن حساب کاربری شما خواهد شد.")
                        Text("5. ما حق داریم در هر زمان تغییراتی در این قوانین ایجاد کنیم.")
                    }
                },
                confirmButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("تأیید")
                    }
                }
            )
        }
    }
    // پیام خطا
    if (showError) {
        Toast.makeText(context, "لطفاً تمامی فیلدها را پر کنید.", Toast.LENGTH_SHORT).show()
        showError = false
    }
}

@Preview(showBackground = true)
@Composable
fun ShowAcceptTerms() {
    val navController = rememberNavController()
    AcceptTermsScreen(navController, phone = "", password = "")
}

@Preview(showBackground = true)
@Composable
private fun ShowLogin() {
    val navController = rememberNavController()
    profileScreen(navController)
}