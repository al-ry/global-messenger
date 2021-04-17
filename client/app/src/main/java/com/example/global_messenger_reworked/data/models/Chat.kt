package com.example.global_messenger_reworked.data.models

import com.google.gson.annotations.SerializedName

data class Chat (
        @SerializedName("friendPhone")
        val friendPhone : String = "",

        @SerializedName("friendName")
        val friendName : String = "",

        @SerializedName("sender")
        val senderPhone : String = "",

        @SerializedName("message")
        var message : String = "",

        @SerializedName("date ")
        val date  : String = ""

)