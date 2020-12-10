package com.example.vd.GalleryUploadFile

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MyAPI {

    @Multipart
    @POST("/users/c_upload_photo")
    @Headers("x-auth-token:hello")
    fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("image") desc: RequestBody
    ): Call<UploadResponse>

    companion object {
        operator fun invoke(): MyAPI {
            return Retrofit.Builder()
                .baseUrl("https://vk-backend.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyAPI::class.java)
        }
    }
}