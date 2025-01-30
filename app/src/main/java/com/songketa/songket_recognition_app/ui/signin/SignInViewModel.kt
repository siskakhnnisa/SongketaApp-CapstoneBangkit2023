package com.songketa.songket_recognition_app.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.songketa.songket_recognition_app.data.Repository
import com.songketa.songket_recognition_app.data.model.User
import kotlinx.coroutines.launch

class SignInViewModel (private val repository: Repository): ViewModel(){
    fun login(email: String, password: String) = repository.login(email, password)

    fun saveSession(user: User) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}