package com.example.shop_app_project.data.view_model

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.shop_app_project.data.models.product.Category
import com.example.shop_app_project.data.models.product.ProductModel
import com.example.shop_app_project.data.utils.UtilsRetrofit
import com.example.shop_app_project.data.utils.UtilsRetrofit.api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.ByteArrayOutputStream
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException


data class OPT_Model(
    val phone: String,
    val otp: String,
)

sealed class ApiState<out T> {
    object Loading : ApiState<Nothing>()
    data class Success<out T>(val data: T) : ApiState<T>()
    data class Error(val message: String) : ApiState<Nothing>()
}


class UserViewModel(application: Application) : AndroidViewModel(application) {
    var registrationResult = mutableStateOf("")
    var login_result = mutableStateOf("")
    var products = mutableStateOf<List<ProductModel>>(arrayListOf())
    var category = mutableStateOf<List<Category>>(arrayListOf())
    var userProducts = mutableStateOf<List<ProductModel>>(arrayListOf())

    //chech_for_login
    var isLoggedIn by mutableStateOf(false)

    private val shoppingCartViewModel = ShoppingCartViewModel(application)

    private val sharedPreferences =
        application.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)


    //checkLogin
    fun checkCredentials(): Boolean {
        val savedCredentials = getSavedCredentials()

        return savedCredentials.first.isNotBlank() && savedCredentials.second.isNotBlank()
    }


    fun getAllProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = withTimeout(30_000) {
                    UtilsRetrofit.api.getAllProducts()
                }

                if (response.isSuccessful && response.body() != null) {
                    Log.d("UserViewModel", "Products fetched successfully: ${response.body()}")
                    products.value = response.body()!!
                } else {
                    Log.e(
                        "UserViewModel",
                        "Failed to fetch products: ${response.errorBody()?.string()}"
                    )
                }

            } catch (e: IOException) {
                Log.e("UserViewModel", "Network error occurred while fetching products.", e)
                registrationResult.value = "Network error occurred."
                return@launch
            } catch (e: HttpException) {
                Log.e(
                    "UserViewModel",
                    "HTTP error occurred while fetching products: ${e.code()}",
                    e
                )
                registrationResult.value = "HTTP error occurred: ${e.code()}"
                return@launch

            }


//            getCategories()
        }
    }


    suspend fun getProductById(context: Context, productId: Int): ProductModel? {
        return try {
            val response = UtilsRetrofit.api.getProductById(productId)
            if (response.isSuccessful) {
                response.body()
            } else {
                Toast.makeText(context, "Failed to fetch product", Toast.LENGTH_SHORT).show()
                null
            }
        } catch (e: IOException) {
            Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show()
            null
        } catch (e: HttpException) {
            Toast.makeText(context, "Server error: ${e.code()}", Toast.LENGTH_SHORT).show()
            null
        }
    }


    //    send Categories request
    fun getCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = try {
                UtilsRetrofit.api.getCategories()
            } catch (e: IOException) {
                Log.e("UserViewModel", "Network error occurred while fetching products.", e)
                return@launch
            } catch (e: HttpException) {
                Log.e(
                    "UserViewModel",
                    "HTTP error occurred while fetching products: ${e.code()}",
                    e

                )
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                category.value = response.body()!!
            }
        }
    }


    //OPT
    fun sendOPT(phone: String, context: Context) {
        viewModelScope.launch {
            val response = try {
                UtilsRetrofit.api.sendOtp(phone)
            } catch (e: IOException) {
                Toast.makeText(context, "this is error in Io", Toast.LENGTH_SHORT).show()
                return@launch
            } catch (e: HttpException) {
                Toast.makeText(context, "This is error in Http Request", Toast.LENGTH_SHORT)
                    .show()
                return@launch
            }

        }
    }

    fun verifyOtp(phone: String, otp: String, context: Context, navController: NavController) {
        viewModelScope.launch {
            val response = try {
                UtilsRetrofit.api.verifyOtp(phone, otp)
            } catch (e: IOException) {
                Toast.makeText(context, "Error in network", Toast.LENGTH_SHORT).show()
                return@launch
            } catch (e: HttpException) {
                Toast.makeText(context, "Error in request: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                Toast.makeText(context, "OTP verified successfully", Toast.LENGTH_SHORT).show()
                navController.navigate("home")
            } else {
                // Log the error body or message for better debugging
                Log.e("VerifyOTP", "Error: ${response.errorBody()?.string()}")
                Toast.makeText(
                    context,
                    "Error: ${response.errorBody()?.string()}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    //RegisterUser
    fun registerUser(phone: String, password: String) {
        viewModelScope.launch {
            val response = try {
                UtilsRetrofit.api.registerUser(phone, password)
            } catch (e: IOException) {
                Log.e("UserViewModel", "Network error occurred while registering user.", e)
                registrationResult.value = "Network error occurred."
                return@launch
            } catch (e: HttpException) {
                Log.e(
                    "UserViewModel",
                    "HTTP error occurred while registering user: ${e.code()}",
                    e
                )
                registrationResult.value = "HTTP error occurred: ${e.code()}"
                return@launch
            }
        }
    }


    fun encodeImageToBase64(context: Context, imageUri: Uri): String {
        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }

    var job: Job? = null

    fun sendProduct(
        name: String,
        description: String,
        price: String,
        phone: String,
        nameUser: String,
        city: String,
        imageFiles: List<Uri>,
        context: Context
    ) {
        job = viewModelScope.launch(Dispatchers.IO) {
            try {

                Log.d("sendProduct", "Preparing to send product data...")
                val contentResolver = context.contentResolver

                val imageParts = imageFiles.mapIndexed { index, imageFile ->
                    val inputStream = contentResolver.openInputStream(imageFile)
                    val fileBytes = inputStream?.readBytes()
                        ?: throw IllegalArgumentException("Cannot read image file")
                    val imageRequestBody =
                        fileBytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData(
                        "images",
                        "image$index.jpg",
                        imageRequestBody
                    )
                }


                Log.d("ImageParts", "Image parts count: ${imageParts.size}")
                imageParts.forEachIndexed { index, part ->
                    Log.d("ImageParts", "Image $index: ${part.body}")
                }

                Log.d("sendProduct", "Name: $name")
                Log.d("sendProduct", "Description: $description")
                Log.d("sendProduct", "Price: $price")
                Log.d("sendProduct", "Phone: $phone")
                Log.d("sendProduct", "NameUser: $nameUser")
                Log.d("sendProduct", "City: $city")


                val nameBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
                val descriptionBody =
                    description.toRequestBody("text/plain".toMediaTypeOrNull())
                val priceBody = price.toRequestBody("text/plain".toMediaTypeOrNull())
                val phoneBody = phone.toRequestBody("text/plain".toMediaTypeOrNull())
                val nameUserBody = nameUser.toRequestBody("text/plain".toMediaTypeOrNull())
                val cityBody = city.toRequestBody("text/plain".toMediaTypeOrNull())

                Log.d("sendProduct", "Calling API to send product...")
                withTimeout(30_000) {
                    val response = UtilsRetrofit.api.addProduct(
                        name = nameBody,
                        description = descriptionBody,
                        nameUser = nameUserBody,
                        phone = phoneBody,
                        price = priceBody,
                        city = cityBody,
                        images = imageParts
                    )

                    if (response.isSuccessful) {
                        Log.d("sendProduct", "Product saved successfully.")
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "محصول با موفقیت ذخیره شد", Toast.LENGTH_LONG)
                                .show()
                        }

                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("sendProduct", "Error response: $errorBody")
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                context,
                                "خطا در ذخیره محصول: $errorBody",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }

                    }
                }
            } catch (e: CancellationException) {
                Log.e("sendProduct", "Job cancelled: ${e.message}")
            } catch (e: Exception) {
                Log.e("sendProduct", "Unexpected error occurred", e)
            }
        }
    }

    fun cancelJob() {
        job?.cancel()
    }


    fun getUserProducts(phone: String, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = try {
                UtilsRetrofit.api.getUserProducts(phone)
            } catch (e: IOException) {
                Log.e(
                    "UserViewModel",
                    "Network error occurred while fetching user products.",
                    e
                )
                Toast.makeText(context, "Network error occurred.", Toast.LENGTH_SHORT).show()
                return@launch
            } catch (e: HttpException) {
                Log.e(
                    "UserViewModel",
                    "HTTP error occurred while fetching user products: ${e.code()}",
                    e
                )
                when (e.code()) {
                    502 -> {
                        Toast.makeText(
                            context,
                            "Server error (502). Please try again later.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {
                        Toast.makeText(
                            context,
                            "HTTP error occurred: ${e.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                return@launch
            } catch (e: Exception) {
                Log.e(
                    "UserViewModel",
                    "Unexpected error occurred while fetching user products.",
                    e
                )
                Toast.makeText(context, "Unexpected error occurred.", Toast.LENGTH_SHORT).show()
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                userProducts.value = response.body()!!
            } else {
                Log.e(
                    "UserViewModel",
                    "Failed to fetch user products: ${response.errorBody()?.string()}"
                )
                Toast.makeText(context, "Failed to fetch user products.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    fun saveCredentials(username: String, password: String, phone: String, location: String) {
        with(sharedPreferences.edit()) {
            putString("username", username)
            putString("password", password)
            putString("phone", phone)
            putString("location", location)
            apply()
        }
    }


    fun getSavedCredentials(): Quadruple<String, String, String, String> {
        val username = sharedPreferences.getString("username", "") ?: ""
        val password = sharedPreferences.getString("password", "") ?: ""
        val phone = sharedPreferences.getString("phone", "") ?: ""
        val location = sharedPreferences.getString("location", "") ?: ""
        return Quadruple(username, password, phone, location)
    }

    data class Quadruple<out A, out B, out C, out D>(
        val first: A,
        val second: B,
        val third: C,
        val fourth: D
    )


}




