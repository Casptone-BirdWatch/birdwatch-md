package com.capstonebangkit.birdwatch.view.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstonebangkit.birdwatch.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {
    fun addBookmark(query: String) {
        viewModelScope.launch {
            try {
                val apiService = ApiConfig.getApiService()
                val response = apiService.addBookmark(query)
                if (response.isSuccessful) {
                    Log.d("AddBookmark", "Berhasil menambahkan bookmark")
                } else {
                    Log.e("AddBookmark", "Gagal menambahkan bookmark: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("AddBookmark", "Exception: ${e.message}")
            }
        }
    }
}