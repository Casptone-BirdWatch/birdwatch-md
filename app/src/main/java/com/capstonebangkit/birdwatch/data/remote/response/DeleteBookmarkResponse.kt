package com.capstonebangkit.birdwatch.data.remote.response

import com.google.gson.annotations.SerializedName

data class DeleteBookmarkResponse(
    @field:SerializedName("id")
    val id: String? = null
)
