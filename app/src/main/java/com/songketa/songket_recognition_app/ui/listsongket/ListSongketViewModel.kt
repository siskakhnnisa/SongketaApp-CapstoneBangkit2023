package com.songketa.songket_recognition_app.ui.listsongket

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.songketa.songket_recognition_app.data.Repository
import com.songketa.songket_recognition_app.data.Result
import com.songketa.songket_recognition_app.data.api.ApiConfig
import com.songketa.songket_recognition_app.data.model.User
import com.songketa.songket_recognition_app.data.response.DatasetItem
import com.songketa.songket_recognition_app.data.response.LoginResponse
import retrofit2.HttpException

class ListSongketViewModel(private val repository: Repository) : ViewModel() {
    fun getStories() = repository.getSongket()

    fun getSession(): LiveData<User> {
        return repository.getSession().asLiveData()
    }

    fun searchSongket(query: String): LiveData<Result<List<DatasetItem>>> = liveData {
        emit(Result.Loading)
        try {
            val songketList: List<DatasetItem>
            val response = ApiConfig.getApiService().getListSongket()
            songketList = response.dataset

            val filteredList = songketList.filter {
                it.fabricname.contains(query, true) || it.origin.contains(query, true)
            }

            emit(Result.Success(filteredList))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error("Cannot Get Stories: $errorMessage"))
        } catch (e: Exception) {
            emit(Result.Error("Something Error"))
        }
    }

}

