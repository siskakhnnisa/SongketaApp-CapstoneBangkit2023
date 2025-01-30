package com.songketa.songket_recognition_app.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class UserResponse(
	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null
) : Parcelable
