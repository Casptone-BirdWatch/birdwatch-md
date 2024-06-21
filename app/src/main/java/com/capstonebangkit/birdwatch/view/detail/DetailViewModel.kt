package com.capstonebangkit.birdwatch.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstonebangkit.birdwatch.data.remote.request.AddBookmarkRequest
import com.capstonebangkit.birdwatch.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    fun addBookmark(predictionId: String) {
        viewModelScope.launch {
            try {
                val apiService = ApiConfig.getApiService()
                val request = AddBookmarkRequest(predictionId)
                val response = apiService.addBookmark(request)

                if (response.isSuccessful) {
                    _toastMessage.postValue("Bookmark berhasil ditambahkan")
                } else {
                    _toastMessage.postValue("Failed to add bookmark: ${response.message()}")
                }
            } catch (e: Exception) {
                _toastMessage.postValue("Exception: ${e.message}")
            }
        }
    }
}
