package com.example.shop_app_project.data.models.Profile

import okhttp3.MultipartBody

data class ProdfileModel(
    val phone: String,
    val id: Int,
    val password: String
)


data class UserProfile(
    val id: Int,
    val name: String,
    val image: String,
    val gender: String?,
    val bio: String?,
    val address: String?
)
data class EditProfileRequest(
    val name: String,
    val image: MultipartBody.Part,
    val credit: Int
)
