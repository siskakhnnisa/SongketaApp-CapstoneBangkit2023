package com.songketa.songket_recognition_app.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.songketa.songket_recognition_app.data.database.SongketDao
import com.songketa.songket_recognition_app.data.database.SongketDatabase
import com.songketa.songket_recognition_app.data.database.SongketEntity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class SongketRepository (application: Application) {
    private val dao: SongketDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val db = SongketDatabase.getDatabase(application)
        dao = db.songketDao()
    }
    fun getSongket(): LiveData<List<SongketEntity>> = dao.getSongket()

//    fun insertSongket(users: SongketEntity){
//        executorService.execute { dao.insertSongket(users) }
//    }
//    fun deleteSongket(users: SongketEntity) {
//        executorService.execute { dao.deleteSongket(users) }
//    }
//    fun updateSongket(users: SongketEntity) {
//        executorService.execute { dao.updateSongket(users) }
//    }

}