package com.example.kotlinmessenger.retrofit

import io.reactivex.Observable
import android.text.LoginFilter
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*


interface INodeJS {
    @POST("register")
    @FormUrlEncoded
    fun RegisterUser(@Field("name") username: String,
                     @Field("telephone") phone: String,
                     @Field("password") password: String): Observable<String>

    @POST("login")
    @FormUrlEncoded
    fun LogInUser(@Field("telephone") phone: String,
                      @Field("password") password: String): Observable<String>

    @POST("home")
    fun CheckSession(@Header("Cookie") sessionIdAndToken: String? ): Observable<String>
 }