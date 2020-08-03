package com.example.kotlinmessenger.retrofit

import com.example.kotlinmessenger.storage.Constants
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitClient {
    private var ourInstance:Retrofit ?= null;
    val instance : Retrofit
        get() {
            if (ourInstance == null)
                ourInstance = Retrofit.Builder()
                .baseUrl(Constants.URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return ourInstance!!
        }
}