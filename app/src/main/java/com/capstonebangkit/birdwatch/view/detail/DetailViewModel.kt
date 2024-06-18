package com.capstonebangkit.birdwatch.view.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstonebangkit.birdwatch.data.remote.response.AddBookmarkRequest
import com.capstonebangkit.birdwatch.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {
    fun addBookmark(predictionId: String) {
        viewModelScope.launch {
            try {
                val apiService = ApiConfig.getApiService()

                val request = AddBookmarkRequest(predictionId)
                val response = apiService.addBookmark(request)

                if (response.isSuccessful) {
                    Log.d("AddBookmark", "Successfully added bookmark")
                } else {
                    Log.e("AddBookmark", "Failed to add bookmark: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("AddBookmark", "Exception: ${e.message}")
            }
        }
    }
}
