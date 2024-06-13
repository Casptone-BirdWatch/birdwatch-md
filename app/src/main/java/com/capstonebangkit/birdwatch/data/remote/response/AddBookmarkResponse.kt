package com.capstonebangkit.birdwatch.data.remote.response

import com.google.gson.annotations.SerializedName

data class AddBookmarkResponse(

	@field:SerializedName("imageUrl")
	val imageUrl: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)
