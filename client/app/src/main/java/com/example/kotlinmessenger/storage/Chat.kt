package com.example.kotlinmessenger.storage

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Chat (
    @SerializedName("friendPhone")
    val friendPhone : String = "",

    @SerializedName("friendName")
    val friendName : String = "",

    @SerializedName("sender")
    val senderPhone : String = "",

    @SerializedName("message")
    val message : String = "",

    @SerializedName("date ")
    val date  : String = ""

) : Parcelable