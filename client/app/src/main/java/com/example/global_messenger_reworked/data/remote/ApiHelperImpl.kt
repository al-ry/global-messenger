package com.example.global_messenger_reworked.data.remote

import com.example.global_messenger_reworked.data.models.Chat
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService) : ApiHelper {
    override suspend fun getChats(phone: String): Response<List<Chat>> = apiService.getChats(phone)
}