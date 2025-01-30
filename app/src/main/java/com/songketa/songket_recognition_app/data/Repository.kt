package com.songketa.songket_recognition_app.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.songketa.songket_recognition_app.data.api.ApiConfig
import com.songketa.songket_recognition_app.data.api.ApiService
import com.songketa.songket_recognition_app.data.model.User
import com.songketa.songket_recognition_app.data.response.DatasetItem
import com.songketa.songket_recognition_app.data.response.DetailSongketResponse
import com.songketa.songket_recognition_app.data.response.LoginResponse
import com.songketa.songket_recognition_app.data.response.MachineLearningResponse
import com.songketa.songket_recognition_app.data.response.RegisterResponse
import com.songketa.songket_recognition_app.utils.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import android.content.Context
import com.songketa.songket_recognition_app.data.database.SongketDao
import com.songketa.songket_recognition_app.data.database.SongketEntity
import com.songketa.songket_recognition_app.data.response.EditUserResponse
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.io.IOException

class Repository private constructor(
    private val context: Context,
    private val userPreference: UserPreferences,
    private val apiService: ApiService,
    private val apiMlService: ApiService,
    private val songketDao : SongketDao,
    ) {
    fun getApplication(): Application {
        return context.applicationContext as Application
    }
    fun login(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            if (response.error == false) {
                val user = User(
                    userId = response.loginResult.userId,
                    name = response.loginResult.name,
                    email = response.loginResult.email,
                    phone = response.loginResult.phone,
                    token = response.loginResult.token,
                    isLogin = true
                )
                userPreference.saveSession(user)
                emit(Result.Success(response))
            } else {
                emit(Result.Error(response.message))
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error("Login Failed: $errorMessage"))
        } catch (e: Exception) {
            emit(Result.Error("Something Error"))
        }
    }

    fun register(
        name: String,
        email: String,
        phone: String,
        password: String
    ): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(name, email, phone, password)
            if (response.error == false) {
                emit(Result.Success(response))
            } else {
                emit(Result.Error(response.message))
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, RegisterResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error("Registration Failed: $errorMessage"))
        } catch (e: Exception) {
            emit(Result.Error("Something Error"))
        }
    }

    fun editUser(
        id: String,
        name: String,
        phone: String,
        password: String
    ): LiveData<Result<EditUserResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.editUser(id, name, phone, password)
            if (response.error == false) {
                emit(Result.Success(response))
            } else {
                emit(Result.Error(response.message))
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, EditUserResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error("Edit Failed: $errorMessage"))
        } catch (e: Exception) {
            emit(Result.Error("Something Error"))
        }
    }

        fun postImage(file: MultipartBody.Part): LiveData<Result<MachineLearningResponse>> =
            liveData {
                emit(Result.Loading)
                try {
                    val response = apiMlService.postImage(file)
                    emit(Result.Success(response))
                } catch (e: IOException) {
                    Log.d("postImage", "IOException: ${e.message}")
                    emit(Result.Error("Network error: ${e.message}"))
                } catch (e: HttpException) {
                    Log.d("postImage", "HttpException: ${e.message}")
                    emit(Result.Error("HTTP error: ${e.message}"))
                } catch (e: Exception) {
                    Log.d("postImage", "Unknown error: ${e.message}")
                    emit(Result.Error("An unknown error occurred"))
                }
            }

        fun getSongket(): LiveData<Result<List<DatasetItem>>> = liveData {
            emit(Result.Loading)
            try {
                val songketList: List<DatasetItem>
                val user = runBlocking { userPreference.getSession().first() }
                val response = ApiConfig.getApiService()
                val songketResponse = response.getListSongket()
                songketList = songketResponse.dataset

                if (songketResponse.error == false) {
                    emit(Result.Success(songketList))
                } else {
                    emit(Result.Error(songketResponse.message))
                }
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
                val errorMessage = errorBody.message
                emit(Result.Error("Cannot Get Stories: $errorMessage"))
            } catch (e: Exception) {
                emit(Result.Error("Something Error"))
            }
        }

        fun getDetailStory(id: String): LiveData<Result<DetailSongketResponse>> = liveData {
            emit(Result.Loading)
            try {
                val response = ApiConfig.getApiService()
                val detailStoryResponse = response.getDetailStory(id)

                if (detailStoryResponse != null) {
                    emit(Result.Success(detailStoryResponse))
                } else {
                    emit(Result.Error(detailStoryResponse.message))
                }
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
                val errorMessage = errorBody.message
                emit(Result.Error("Can't Get Stories: $errorMessage"))
            } catch (e: Exception) {
                emit(Result.Error("Something Error"))
            }
        }

        suspend fun saveSession(user: User) {
            userPreference.saveSession(user)
        }

        fun getSession(): Flow<User> {
            return userPreference.getSession()
        }

        suspend fun logout() {
            userPreference.logout()
        }

        //Database Local
        suspend fun saveBookmark(favoriteUserEntity: SongketEntity) {
            songketDao.insert(favoriteUserEntity)
        }

        suspend fun delete(favoriteUserEntity: SongketEntity) {
            songketDao.delete(favoriteUserEntity)
        }

        fun getBookmarkSongket(): LiveData<List<SongketEntity>> = songketDao.getSongket()

        fun isFavorite(songket: String): LiveData<SongketEntity> {
            return songketDao.isFavorite(songket)
        }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            context: Context,
            userPreference: UserPreferences,
            apiService: ApiService,
            apiMlService: ApiService,
            songketDao: SongketDao
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(context,userPreference, apiService, apiMlService, songketDao)
            }.also { instance = it }
    }
}