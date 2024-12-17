package com.example.shop_app_project.Home_page.Main.SharedPreferencesManager

import android.content.Context
import android.content.SharedPreferences
import com.example.shop_app_project.Home_page.Main.Screen_Item.ProductModel
import com.example.shop_app_project.data.models.product.PorductModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SharedPreferencesManager {
    private const val PREFS_NAME = "cart_prefs"
    private const val CART_KEY = "cart_items"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveCartItems(
        context: Context,
        cartItems: List<PorductModel>
    ) {
        val json = Gson().toJson(cartItems)
        getPreferences(context).edit().putString(CART_KEY, json).apply()
    }

    fun loadCartItems(context: Context): List<PorductModel> {
        val json = getPreferences(context).getString(CART_KEY, null) ?: return emptyList()
        val type = object :
            TypeToken<List<PorductModel>>() {}.type
        return Gson().fromJson(json, type)
    }

    fun clearCartItems(context: Context) {
        getPreferences(context).edit().remove(CART_KEY).apply()
    }
}
