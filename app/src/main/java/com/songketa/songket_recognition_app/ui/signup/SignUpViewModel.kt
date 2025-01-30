package com.songketa.songket_recognition_app.ui.signup

import androidx.lifecycle.ViewModel
import com.songketa.songket_recognition_app.data.Repository

class SignUpViewModel (private val repository: Repository): ViewModel() {
    fun register(name: String, email: String, phone: String, password: String) =
        repository.register(name, email, phone, password)
}