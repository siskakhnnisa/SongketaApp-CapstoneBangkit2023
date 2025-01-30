package com.songketa.songket_recognition_app.ui.detailsongket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.songketa.songket_recognition_app.data.Repository
import com.songketa.songket_recognition_app.data.database.SongketEntity
import kotlinx.coroutines.launch

class DetailSongketViewModel(private val repository: Repository) : ViewModel() {

    fun getDetailStory(id : String) = repository.getDetailStory(id)

    fun isFavorite(songket: String)=
        repository.isFavorite(songket)

    fun saveFavorite(songketEntity: SongketEntity){
        viewModelScope.launch { repository.saveBookmark(songketEntity) }
    }

    fun deleteFavorite(songketEntity: SongketEntity){
        viewModelScope.launch { repository.delete(songketEntity) }
    }

}