package com.example.kotlinmessenger.Response

import com.google.gson.annotations.SerializedName


data class User (
    @SerializedName("id_user")
    val id : String,

    @SerializedName("telephone")
    val telephone : String,

    @SerializedName("name")
    val name : String,

    @SerializedName("crypted_password")
    val password : String,

    @SerializedName("salt_password")
    val salt_password : String
)