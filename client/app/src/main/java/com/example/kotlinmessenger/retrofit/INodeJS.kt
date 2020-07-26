package com.example.kotlinmessenger.retrofit

import com.example.kotlinmessenger.Response.CookieStorage
import com.example.kotlinmessenger.Response.User
import io.reactivex.Observable
import retrofit2.http.*
import retrofit2.Call


interface INodeJS {
    @POST("register")
    @FormUrlEncoded
    fun RegisterUser(@Field("name") username: String,
                     @Field("telephone") phone: String,
                     @Field("password") password: String): Call<CookieStorage>

    @POST("login")
    @FormUrlEncoded
    fun LogInUser(@Field("telephone") phone: String,
                      @Field("password") password: String): Call<CookieStorage>

    @GET("home")
    fun CheckSession(@Header("Cookie") sessionIdAndToken: String? ): Call<Void>

    @GET("logout")
    fun LogOut(@Header("Cookie") sessionIdAndToken : String) : Call<Void>

    @GET("search")
    fun FindUser (@Query("telephone") phone : String) : Call<List<User>>

 }