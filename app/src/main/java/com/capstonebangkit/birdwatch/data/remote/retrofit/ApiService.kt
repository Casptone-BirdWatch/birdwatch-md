package com.capstonebangkit.birdwatch.data.remote.retrofit

import com.capstonebangkit.birdwatch.data.remote.response.AddBookmarkResponse
import com.capstonebangkit.birdwatch.data.remote.response.PredictResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @Multipart
    @POST("predict")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): Response<PredictResponse>

    @POST("addBookmark")
    suspend fun addBookmark(
        @Query("query") query: String
    ): Response<AddBookmarkResponse>

    @GET("searchBirds")
    fun searchBirds(@Query("query") query: String): Call<List<PredictResponse>>
}