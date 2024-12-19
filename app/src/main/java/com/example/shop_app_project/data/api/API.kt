package com.example.shop_app_project.data.api

import com.example.shop_app_project.data.models.Profile.ProdfileModel
import com.example.shop_app_project.data.models.product.Category
import com.example.shop_app_project.data.models.product.ProductModel
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface API {

    @GET("getallProduct/")
    suspend fun getAllProducts(): Response<List<ProductModel>>


    @GET("products/{id}")
    suspend fun getProductById(@Path("id") productId: Int): Response<ProductModel>


    @FormUrlEncoded
    @POST("")
    suspend fun sendProduct(
        @Field("image") image: String,
        @Part("name") name: String,
        @Part("description") description: String,
        @Part("price") price: String,
    ): Response<ProductModel>

    @FormUrlEncoded
    @POST("registerUser/")
    suspend fun registerUser(
        @Field("phone") phone: String,
        @Field("password") password: String
    ): Response<ProdfileModel>

    @GET("GetCategories")
    suspend fun getCategories(): Response<List<Category>>
}