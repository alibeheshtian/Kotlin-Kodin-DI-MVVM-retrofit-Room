package com.shenoto.domain.services

import com.example.data.api.model.MovieModel
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {
    @GET("movies-coming-soon.json")
    fun movies(): Single<List<MovieModel>>

    @GET
    fun test(@Url url:String): Single<Any>
}

