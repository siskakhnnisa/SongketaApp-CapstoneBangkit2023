package com.songketa.songket_recognition_app.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("songket_name")
    val name: String,

    @field:SerializedName("songket_motif")
    val motif: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("message")
    val message: String

): Parcelable
