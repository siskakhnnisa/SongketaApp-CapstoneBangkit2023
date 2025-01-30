package com.songketa.songket_recognition_app.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class DetailSongketResponse(

	@field:SerializedName("datasetItem")
	val datasetItem: DatasetItemDetail,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
) : Parcelable

@Parcelize
data class DatasetItemDetail(

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
