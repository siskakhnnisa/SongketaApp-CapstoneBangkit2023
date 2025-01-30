package com.songketa.songket_recognition_app.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.songketa.songket_recognition_app.data.Repository
import com.songketa.songket_recognition_app.data.model.User
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: Repository) : ViewModel() {

    fun getSession(): LiveData<User> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun editUser(id: String, name: String, phone: String, password: String) =
        repository.editUser(id, name, phone, password)

}