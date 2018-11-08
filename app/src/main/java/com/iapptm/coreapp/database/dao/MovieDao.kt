package com.iapptm.coreapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.iapptm.coreapp.database.entity.MovieEntity

@Dao
interface MovieDao {

    @Query("SELECT * from movies_favorite ORDER BY movie ASC")
    fun getAllMovies(): LiveData<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: MovieEntity)

    @Query("DELETE FROM movies_favorite")
    fun deleteAll()

}