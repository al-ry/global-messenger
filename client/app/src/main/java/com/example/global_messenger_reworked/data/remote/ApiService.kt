package com.example.global_messenger_reworked.data.remote

import com.example.global_messenger_reworked.data.models.Chat
import com.example.global_messenger_reworked.data.models.Message
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("chats")
    suspend fun getChats(@Query("userPhone") phone: String) : Response<List<Chat>>

    @GET("getHistory")
    suspend fun getHistory(
            @Query("senderPhone") senderPhone: String,
            @Query("receiverPhone") receiverPhone: String
    ): Response<List<Message>>

}