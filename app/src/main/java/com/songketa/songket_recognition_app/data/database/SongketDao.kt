package com.songketa.songket_recognition_app.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SongketDao {
    @Query("SELECT * FROM songket")
    fun getSongket(): LiveData<List<SongketEntity>>
    @Query("SELECT * FROM songket WHERE idfabric = :idfabric")
    fun isFavorite(idfabric: String):LiveData<SongketEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favoriteSongket: SongketEntity)
    @Delete
    suspend fun delete(favoriteSongket: SongketEntity)
}
