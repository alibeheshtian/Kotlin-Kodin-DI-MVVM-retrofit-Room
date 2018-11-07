package com.iapptm.coreapp.data.services

import com.iapptm.coreapp.data.models.MovieModel
import io.reactivex.Single
import retrofit2.http.GET

interface ApiService {
    @GET("yannski/3019778/raw/dfb34d018165f47b61b3bf089358a3d5ca199d96/movies.json")
    fun movies(): Single<List<MovieModel>>
}

