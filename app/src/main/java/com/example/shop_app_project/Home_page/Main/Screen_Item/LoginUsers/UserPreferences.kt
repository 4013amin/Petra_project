package com.example.shop_app_project.Home_page.Main.Screen_Item.LoginUsers

import android.content.Context

object UserPreferences {
    private const val PREF_NAME = "user_prefs"
    private const val KEY_PHONE = "phone"

    // ذخیره شماره تلفن کاربر
    fun saveUser(context: Context, phone: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_PHONE, phone).apply() // استفاده از apply() برای ذخیره تغییرات
    }

    // بررسی ورود کاربر
    fun isUserLoggedIn(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.contains(KEY_PHONE)
    }

    // دریافت شماره تلفن کاربر
    fun getUserPhone(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_PHONE, null)
    }

    // حذف شماره تلفن کاربر از SharedPreferences
    fun clearUser(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(KEY_PHONE).apply()
    }
}
