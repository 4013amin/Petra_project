package com.example.shop_app_project.data.api

import com.example.shop_app_project.data.models.product.Category
import com.example.shop_app_project.data.models.product.ProductModel
import com.example.shop_app_project.data.models.register.login_model
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface API {

    @GET("getallProduct/")
    suspend fun getAllProducts(): Response<List<ProductModel>>


    @GET("products/{id}")
    suspend fun getProductById(@Path("id") productId: Int): Response<ProductModel>


    @GET("GetCategories")
    suspend fun getCategories(): Response<List<Category>>
}