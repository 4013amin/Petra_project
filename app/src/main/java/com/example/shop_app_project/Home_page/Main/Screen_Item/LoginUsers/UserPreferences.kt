package com.example.shop_app_project.Home_page.Main.Screen_Item.LoginUsers

import android.content.Context

object UserPreferences {
    private const val PREF_NAME = "user_prefs"
    private const val KEY_PHONE = "phone"
    private const val KEY_PASSWORD = "password"

    fun saveUser(context: Context, phone: String, password: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_PHONE, phone).putString(KEY_PASSWORD, password).apply()
    }

    fun isUserLoggedIn(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.contains(KEY_PHONE) && prefs.contains(KEY_PASSWORD)
    }



}
