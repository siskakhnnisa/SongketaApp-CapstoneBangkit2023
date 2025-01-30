package com.songketa.songket_recognition_app.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class MachineLearningResponse(

	@field:SerializedName("class_pattern")
	val classPattern: String,

	@field:SerializedName("dataset_info")
	val datasetInfo: DatasetInfo
) : Parcelable

@Parcelize
data class DatasetInfo(

	@field:SerializedName("origin")
	val origin: String,

	@field:SerializedName("pattern")
	val pattern: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("idfabric")
	val idfabric: String,

	@field:SerializedName("img_url")
	val imgUrl: String,

	@field:SerializedName("fabricname")
	val fabricname: String


) : Parcelable
