package com.example.shop_app_project.data.api

import com.example.shop_app_project.data.models.Profile.ProdfileModel
import com.example.shop_app_project.data.models.Profile.Profile
import com.example.shop_app_project.data.models.product.Category
import com.example.shop_app_project.data.models.product.ProductModel
import com.example.shop_app_project.data.models.register.login_model
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
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
        @Part image: MultipartBody.Part,
        @Part("name") name: String,
        @Part("description") description: String,
        @Part("price") price: String,
    )

    @FormUrlEncoded
    @POST("registerUser/")
    suspend fun registerUser(
        @Field("phone") phone: String,
        @Field("password") password: String
    ): Response<ProdfileModel>

    @GET("GetCategories")
    suspend fun getCategories(): Response<List<Category>>
}