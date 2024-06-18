package com.capstonebangkit.birdwatch.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PredictResponse(

    @field:SerializedName("Genus")
    val genus: String? = null,

    @field:SerializedName("JenisBurung")
    val jenisBurung: String? = null,

    @field:SerializedName("imageUrl")
    val imageUrl: String? = null,

    @field:SerializedName("Deskripsi")
    val deskripsi: String? = null,

    @field:SerializedName("Famili")
    val famili: String? = null,

    @field:SerializedName("id")
    val id: String? = null
): Parcelable
