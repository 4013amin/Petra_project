package com.example.shop_app_project.data.view_model

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.shop_app_project.Home_page.Main.SharedPreferencesManager.SharedPreferencesManager
import com.example.shop_app_project.data.models.product.ProductModel
import kotlinx.coroutines.launch

class SavedProductsViewModel(application: Application) : AndroidViewModel(application) {

    private val _savedProducts = mutableStateListOf<ProductModel>()
    val savedProducts: List<ProductModel> get() = _savedProducts

    init {
        viewModelScope.launch {
            _savedProducts.addAll(SharedPreferencesManager.loadSavedProducts(getApplication()))
        }
    }

    fun saveProduct(product: ProductModel) {
        if (!_savedProducts.contains(product)) {
            _savedProducts.add(product)
            saveSavedProducts()
        }
    }

    fun removeSavedProduct(product: ProductModel) {
        _savedProducts.remove(product)
        saveSavedProducts()
    }

    private fun saveSavedProducts() {
        SharedPreferencesManager.saveSavedProducts(getApplication(), _savedProducts)
    }
}
