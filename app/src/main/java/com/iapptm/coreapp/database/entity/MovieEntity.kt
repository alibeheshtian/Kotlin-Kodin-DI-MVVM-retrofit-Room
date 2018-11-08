package com.iapptm.coreapp.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies_favorite")
class MovieEntity(@PrimaryKey @ColumnInfo(name = "movieId") val movieId: String ,@ColumnInfo(name = "movie") val movie: String)
