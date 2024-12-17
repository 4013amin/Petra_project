package com.example.shop_app_project.data.view_model

import android.app.Application
import android.content.Context
import android.util.Log
import android.util.Pair
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.shop_app_project.data.models.product.Category
import com.example.shop_app_project.data.models.product.PorductModel
import com.example.shop_app_project.data.models.register.login_model
import com.example.shop_app_project.data.utils.UtilsRetrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class UserViewModel(application: Application) : AndroidViewModel(application) {
    var registrationResult = mutableStateOf("")
    var login_result = mutableStateOf("")
    var products = mutableStateOf<List<PorductModel>>(arrayListOf())
    var category = mutableStateOf<List<Category>>(arrayListOf())


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
            val response = try {
                UtilsRetrofit.api.getAllProducts()
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

            if (response.isSuccessful && response.body() != null) {
                Log.d("UserViewModel", "Products fetched successfully: ${response.body()}")
                products.value = response.body()!!
            } else {
                Log.e(
                    "UserViewModel",
                    "Failed to fetch products: ${response.errorBody()?.string()}"
                )
            }

//            getCategories()
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

//    // تابع برای اضافه کردن به سبد خرید
//    fun addToCart(product: PorductModel) {
//        shoppingCartViewModel.addToCart(product)
//    }

}




