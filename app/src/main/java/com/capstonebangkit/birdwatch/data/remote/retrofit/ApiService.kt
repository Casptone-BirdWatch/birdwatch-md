package com.capstonebangkit.birdwatch.data.remote.retrofit

import com.capstonebangkit.birdwatch.data.remote.request.AddBookmarkRequest
import com.capstonebangkit.birdwatch.data.remote.response.AddBookmarkResponse
import com.capstonebangkit.birdwatch.data.remote.response.BookmarkResponseItem
import com.capstonebangkit.birdwatch.data.remote.response.PredictResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @Multipart
    @POST("predict")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): Response<PredictResponse>

    @POST("bookmark")
    suspend fun addBookmark(
        @Body request: AddBookmarkRequest
    ): Response<AddBookmarkResponse>

    @DELETE("bookmark/{bookmarkId}")
    suspend fun deleteBookmark(
        @Path("bookmarkId") bookmarkId: String
    ): Response<Void>

    @GET("bookmarks")
    suspend fun getBookmarks(): Response<List<BookmarkResponseItem>>
}
