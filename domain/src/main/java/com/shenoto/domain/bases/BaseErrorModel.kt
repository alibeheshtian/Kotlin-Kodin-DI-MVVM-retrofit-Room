package com.shenoto.domain.bases


import com.google.gson.annotations.SerializedName

data class BaseErrorModel<E>(
    @SerializedName("code")
    val code: Int,
    @SerializedName("errorCode")
    val errorCode: String,
    @SerializedName("logRef")
    val logRef: Int,
    @SerializedName("message")
    val message: E
)