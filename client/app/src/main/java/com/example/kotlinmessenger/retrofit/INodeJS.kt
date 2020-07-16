package com.example.kotlinmessenger.retrofit

import io.reactivex.Observable
import android.text.LoginFilter
import retrofit2.http.Field
import retrofit2.http.POST
import retrofit2.http.FormUrlEncoded



interface INodeJS {
    @POST("register")
    @FormUrlEncoded
    fun RegisterUser(@Field("Phone") phone:String,
                    @Field("Password") password:String,
                    @Field("Username") username:String): Observable<String>

    @POST("login")
    @FormUrlEncoded
    fun LogInUser(@Field("Phone") phone:String,
                  @Field("Password") password:String): Observable<String>
 }