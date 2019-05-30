package com.example.data.api.model

import com.google.gson.annotations.SerializedName

data class TokenModel(
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("access_token")
    val accessToken: String
)