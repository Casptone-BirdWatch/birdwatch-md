package com.capstonebangkit.birdwatch.data.remote.response

import com.google.gson.annotations.SerializedName

data class AddBookmarkRequest(
    @SerializedName("predictionId") val predictionId: String
)