package com.example.data.api.model


import com.google.gson.annotations.SerializedName

data class MovieModel(
    @SerializedName("actors")
    val actors: List<String>,
    @SerializedName("averageRating")
    val averageRating: Int,
    @SerializedName("contentRating")
    val contentRating: String,
    @SerializedName("duration")
    val duration: String,
    @SerializedName("genres")
    val genres: List<String>,
    @SerializedName("id")
    val id: String,
    @SerializedName("imdbRating")
    val imdbRating: String,
    @SerializedName("originalTitle")
    val originalTitle: String,
    @SerializedName("poster")
    val poster: String,
    @SerializedName("posterurl")
    val posterurl: String,
    @SerializedName("ratings")
    val ratings: List<Int>,
    @SerializedName("releaseDate")
    val releaseDate: String,
    @SerializedName("storyline")
    val storyline: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("year")
    val year: String
)