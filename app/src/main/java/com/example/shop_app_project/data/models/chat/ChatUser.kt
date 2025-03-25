package com.example.shop_app_project.data.models.chat

data class ChatUsersResponse(
    val users: List<ChatUser>
)

data class ChatUser(
    val phone: String?
)