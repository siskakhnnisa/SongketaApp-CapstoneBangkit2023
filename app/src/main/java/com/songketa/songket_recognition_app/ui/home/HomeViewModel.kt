package com.songketa.songket_recognition_app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.songketa.songket_recognition_app.data.Repository
import com.songketa.songket_recognition_app.data.model.User

class HomeViewModel(private val repository: Repository) : ViewModel() {
    fun getSession(): LiveData<User> {
        return repository.getSession().asLiveData()
    }
    fun getSongket() = repository.getSongket()
}