package com.example.kotlinmessenger.retrofit

import com.example.kotlinmessenger.Response.CookieStorage
import io.reactivex.Observable
import retrofit2.http.*
import retrofit2.Call


interface INodeJS {
    @POST("register")
    @FormUrlEncoded
    fun RegisterUser(@Field("name") username: String,
                     @Field("telephone") phone: String,
                     @Field("password") password: String): Observable<String>

    @POST("login")
    @FormUrlEncoded
    fun LogInUser(@Field("telephone") phone: String,
                      @Field("password") password: String): Call<CookieStorage>

    @POST("home")
    fun CheckSession(@Header("Cookie") sessionIdAndToken: String? ): Observable<String>

    @GET("logout")
    fun LogOut(@Header("Cookie") sessionIdAndToken : String) : Call<Void>

 }