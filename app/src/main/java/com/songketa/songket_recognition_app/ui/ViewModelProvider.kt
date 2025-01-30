package com.songketa.songket_recognition_app.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.songketa.songket_recognition_app.data.Repository
import com.songketa.songket_recognition_app.di.Injection
import com.songketa.songket_recognition_app.ui.bookmark.BookmarkViewModel
import com.songketa.songket_recognition_app.ui.camera.CameraViewModel
import com.songketa.songket_recognition_app.ui.detailsongket.DetailSongketViewModel
import com.songketa.songket_recognition_app.ui.home.HomeViewModel
import com.songketa.songket_recognition_app.ui.listsongket.ListSongketViewModel
import com.songketa.songket_recognition_app.ui.profile.ProfileViewModel
import com.songketa.songket_recognition_app.ui.signin.SignInViewModel
import com.songketa.songket_recognition_app.ui.signup.SignUpViewModel
import com.songketa.songket_recognition_app.ui.splashscreen.SplashViewModel

class ViewModelFactory(private val repository: Repository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ListSongketViewModel::class.java) -> {
                ListSongketViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SignInViewModel::class.java) -> {
                SignInViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                SplashViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailSongketViewModel::class.java) -> {
                DetailSongketViewModel(repository) as T
            }
            modelClass.isAssignableFrom(CameraViewModel::class.java) -> {
                CameraViewModel(repository) as T
            }
            modelClass.isAssignableFrom(BookmarkViewModel::class.java) -> {
                BookmarkViewModel(repository.getApplication()) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (instance == null) {
                synchronized(ViewModelFactory::class.java) {
                    instance = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return instance as ViewModelFactory
        }
    }

}
