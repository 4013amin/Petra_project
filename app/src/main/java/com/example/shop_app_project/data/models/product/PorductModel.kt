package com.example.shop_app_project.data.models.product

import java.io.Serializable

data class PorductModel(
    val category: Category,
    val created_at: String,
    val description: String,
    val image: String,
    val name: String,
    val price: Int,
    val id: Int,
    val pk: Int,
    val stock: Int,
    val updated_at: String,
    )