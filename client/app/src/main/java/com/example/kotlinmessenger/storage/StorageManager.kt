package com.example.kotlinmessenger.storage

import android.content.Context
import android.text.method.TextKeyListener.clear

class StorageManager(context: Context)  {
    private var sharedPreferences = context.getSharedPreferences("myPreferences", 0)

    fun putData(key : String, value : String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getData(key: String): String? {
        return sharedPreferences.getString(key, "false")
    }

    fun deleteData(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }
}