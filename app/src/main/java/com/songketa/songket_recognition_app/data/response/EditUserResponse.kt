package com.songketa.songket_recognition_app.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class EditUserResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("users")
	val users: Users
) : Parcelable

@Parcelize
data class Users(

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("userId")
	val userId: String,

	@field:SerializedName("phone")
	val phone: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("token")
	val token: String,

	@field:SerializedName("username")
	val username: String
) : Parcelable
