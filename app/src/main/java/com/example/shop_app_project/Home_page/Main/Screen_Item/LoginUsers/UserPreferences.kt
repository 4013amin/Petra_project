package com.example.shop_app_project.Home_page.Main.Screen_Item.LoginUsers

import android.content.Context

object UserPreferences {
    private const val PREF_NAME = "user_prefs"
    private const val KEY_PHONE = "phone"

    fun saveUser(context: Context, phone: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_PHONE, phone).apply()
    }

    fun isUserLoggedIn(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.contains(KEY_PHONE)
    }
    fun getUserPhone(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_PHONE, null)
    }

    fun clearUser(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(KEY_PHONE).apply()
    }
}
