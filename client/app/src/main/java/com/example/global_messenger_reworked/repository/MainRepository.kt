package com.example.global_messenger_reworked.repository

import com.example.global_messenger_reworked.data.remote.ApiHelper
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiHelper: ApiHelper) {
    suspend fun getChats(phone: String) = apiHelper.getChats(phone)
}