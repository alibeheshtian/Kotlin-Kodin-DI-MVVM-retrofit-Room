package com.shenoto.domain.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.data.database.dao.MovieDao
import com.example.data.database.entity.MovieEntity


class MovieRepository(private val movieDao: MovieDao)  {

    val allMovies: LiveData<List<MovieEntity>> = movieDao.getAllMovies()

    @WorkerThread
    suspend fun insertToDatabase(movieEntity: MovieEntity) {
        movieDao.insert(movieEntity)
    }

}
