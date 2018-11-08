package com.iapptm.coreapp.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.iapptm.coreapp.database.dao.MovieDao
import com.iapptm.coreapp.database.entity.MovieEntity


class MovieRepository(private val movieDao: MovieDao)  {

    val allMovies: LiveData<List<MovieEntity>> = movieDao.getAllMovies()

    @WorkerThread
    suspend fun insertToDatabase(movieEntity: MovieEntity) {
        movieDao.insert(movieEntity)
    }

}