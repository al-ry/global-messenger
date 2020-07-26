package com.example.kotlinmessenger.Response

import com.google.gson.annotations.SerializedName

data class CookieStorage (
    @SerializedName("connect.sid")
    val cookie : String? = null
)
