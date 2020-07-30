package com.example.kotlinmessenger.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.kotlinmessenger.MyApplication
import com.example.kotlinmessenger.R
import com.example.kotlinmessenger.retrofit.INodeJS
import com.example.kotlinmessenger.storage.Constants
import com.example.kotlinmessenger.storage.StorageManager
import io.socket.client.IO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class LoadingActivity : AppCompatActivity() {

    lateinit var myApi: INodeJS
    private lateinit var storageManager : StorageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)
        checkUserSession()
        storageManager = StorageManager(applicationContext)

        setConnetcion(storageManager.getData(Constants.phoneStorageKey).toString())
    }

    private fun setConnetcion(phone: String) {
        val storageManager = StorageManager(applicationContext)
        MyApplication.m_socket = IO.socket(Constants.url)
        MyApplication.m_socket.connect()
        MyApplication.m_socket.emit("user_connected", phone,
            storageManager.getData(Constants.cookieStorageKey))
    }

    private fun createRetrofitClientToParseJSON(): INodeJS {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.url)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(INodeJS::class.java)
    }

    private fun checkUserSession() {
        storageManager = StorageManager(applicationContext)
        val myApi = createRetrofitClientToParseJSON()
        val cookies =  "connect.sid=" + storageManager.getData(Constants.cookieStorageKey)

        var call = myApi.checkSession(cookies)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(all: Call<Void>, response: Response<Void>)
            {
                if  (response.code() == 200) {

                    val intent = Intent(this@LoadingActivity, LastMessagesActivity::class.java)
                    startActivity(intent)

                }
                else
                    startActivity(Intent(this@LoadingActivity, SignInActivity::class.java))
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@LoadingActivity, "There was an error with authorization",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }
}