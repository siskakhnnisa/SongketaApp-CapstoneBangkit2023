package com.songketa.songket_recognition_app.ui.camera

import androidx.lifecycle.ViewModel
import com.songketa.songket_recognition_app.data.Repository
import okhttp3.MultipartBody

class CameraViewModel (private val repository: Repository): ViewModel(){
    fun postImage(file: MultipartBody.Part) = repository.postImage(file)


}