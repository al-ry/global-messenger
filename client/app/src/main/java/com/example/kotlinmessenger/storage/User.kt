package com.example.kotlinmessenger.storage

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
//    @SerializedName("id_user")
//    val id : String = "",

    @SerializedName("telephone")
    val telephone : String = "",

    @SerializedName("name")
    val name : String = ""
) : Parcelable