package com.example.kotlinmessenger.retrofit

import com.example.kotlinmessenger.storage.CookieStorage
import com.example.kotlinmessenger.storage.Message
import com.example.kotlinmessenger.storage.User
import retrofit2.http.*
import retrofit2.Call

interface INodeJS {
    @POST("register")
    @FormUrlEncoded
    fun registerUser(
        @Field("name") username: String,
        @Field("telephone") phone: String,
        @Field("password") password: String
    ) : Call<CookieStorage>

    @POST("login")
    @FormUrlEncoded
    fun logInUser(@Field("telephone") phone: String,
                  @Field("password") password: String): Call<CookieStorage>

    @GET("home")
    fun checkSession(@Header("Cookie") sessionIdAndToken: String? ): Call<Void>

    @GET("logout")
    fun logOut(@Header("Cookie") sessionIdAndToken: String) : Call<Void>

    @GET("search")
    fun findUser(@Query("telephone") phone: String) : Call<List<User>>

    @GET("chats")
    fun getChats(@Query("userPhone") phone: String) : Call<List<User>>

    @GET("getHistory")
    fun getHistory(
        @Query("senderPhone") senderPhone: String,
        @Query("receiverPhone") receiverPhone: String
    ): Call<List<Message>>

    @GET("addChat")
    fun addChat(
        @Query("userPhone") userPhone: String,
        @Query("friendPhone") friendPhone: String
    ) : Call<Void>

    @GET("deleteChat")
    fun deleteChat(
        @Query("userPhone") userPhone: String,
        @Query("friendPhone") friendPhone: String
    ) : Call<Void>
 }