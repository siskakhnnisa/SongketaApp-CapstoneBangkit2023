package com.songketa.songket_recognition_app.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class SongketDataResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("dataset")
	val dataset: List<DatasetItem>
) : Parcelable

@Parcelize
data class DatasetItem(

	@field:SerializedName("img_url")
	val imgUrl: String,

	@field:SerializedName("origin")
	val origin: String,

	@field:SerializedName("pattern")
	val pattern: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("idfabric")
	val idfabric: String,

	@field:SerializedName("fabricname")
	val fabricname: String
) : Parcelable
