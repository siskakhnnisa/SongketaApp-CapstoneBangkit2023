package com.songketa.songket_recognition_app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SongketEntity::class], version = 1, exportSchema = false)
abstract class SongketDatabase : RoomDatabase(){

    abstract fun songketDao():SongketDao

    companion object {
        @Volatile
        private var instance: SongketDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): SongketDatabase {
            if (instance == null) {
                synchronized(SongketDatabase::class.java) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SongketDatabase::class.java, "favorite_user_database"
                    ).build()
                }
            }
            return instance as SongketDatabase
        }
    }


}