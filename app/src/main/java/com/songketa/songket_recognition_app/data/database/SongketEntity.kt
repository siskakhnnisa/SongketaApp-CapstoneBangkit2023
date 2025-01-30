package com.songketa.songket_recognition_app.data.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "songket")
@Parcelize
class SongketEntity (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "idfabric")
    val idfabric: String = "",

    @ColumnInfo(name="fabricname")
    val fabricname: String? = null,

    @ColumnInfo(name="imgUrl")
    val imgUrl: String? = null,

    @ColumnInfo(name="origin")
    val origin: String? = null
): Parcelable