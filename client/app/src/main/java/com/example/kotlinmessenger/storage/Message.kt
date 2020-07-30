package com.example.kotlinmessenger.storage

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Message(
    @SerializedName("sender")
    val senderPhone : String = "",

    @SerializedName("receiver")
    val receiverPhone : String = "",

    @SerializedName("message")
    val text : String = "",

    @SerializedName("date")
    val date : String = ""
) : Parcelable