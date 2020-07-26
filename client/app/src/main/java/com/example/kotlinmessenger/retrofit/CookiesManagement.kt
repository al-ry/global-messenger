package com.example.kotlinmessenger.retrofit

import android.content.Context
import android.content.SharedPreferences
import android.text.method.TextKeyListener.clear

class CookiesManagement(context: Context)  {
    private var sharedPreferences = context.getSharedPreferences("myPreferences", 0)

    fun PutCookie(cookies : String) {
        sharedPreferences.edit().putString("cookies", cookies).apply()
    }

    fun GetCookie() : String? {
        return sharedPreferences.getString("cookies", null)
    }

    fun DeleteCookie()
    {
        sharedPreferences.edit().remove("cookies").commit()
    }


}