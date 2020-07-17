package com.example.kotlinmessenger.retrofit

import io.reactivex.Observable
import android.text.LoginFilter
import retrofit2.http.Field
import retrofit2.http.POST
import retrofit2.http.FormUrlEncoded



interface INodeJS {
    @POST("register")
    @FormUrlEncoded
    fun RegisterUser(@Field("username") username: String,
                     @Field("telephone") phone: String,
                     @Field("password") password: String): Observable<String>

    @POST("login")
    @FormUrlEncoded
    fun LogInUser(@Field("telephone") phone: String,
                  @Field("password") password: String): Observable<String>
 }