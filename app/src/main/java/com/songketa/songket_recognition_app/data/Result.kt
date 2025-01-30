package com.songketa.songket_recognition_app.data

sealed class Result<out R> private constructor() {

    object Loading : Result<Nothing>()
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val error: String) : Result<Nothing>()

}
