package com.capstonebangkit.birdwatch.view.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstonebangkit.birdwatch.data.remote.response.BookmarkResponseItem
import com.capstonebangkit.birdwatch.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _bookmarks = MutableLiveData<List<BookmarkResponseItem?>>()
    val bookmarks: LiveData<List<BookmarkResponseItem?>>
        get() = _bookmarks

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    fun getBookmarks() {
        viewModelScope.launch {
            try {
                val apiService = ApiConfig.getApiService()
                val response = apiService.getBookmarks()
                if (response.isSuccessful) {
                    _bookmarks.postValue(response.body())
                } else {
                    _toastMessage.postValue("Failed to fetch bookmarks: ${response.message()}")
                }
            } catch (e: Exception) {
                _toastMessage.postValue("Exception: ${e.message}")
            }
        }
    }

    fun removeBookmarkById(bookmarkId: String) {
        _bookmarks.value = _bookmarks.value?.filterNot { it?.id == bookmarkId }
    }
}
