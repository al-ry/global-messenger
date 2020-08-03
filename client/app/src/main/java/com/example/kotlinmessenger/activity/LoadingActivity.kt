package com.example.kotlinmessenger.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.kotlinmessenger.SocketManager
import com.example.kotlinmessenger.R
import com.example.kotlinmessenger.retrofit.INodeJS
import com.example.kotlinmessenger.retrofit.RetrofitClient
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

    private lateinit var myApi: INodeJS
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)
        checkUserSession()
    }

    private fun checkUserSession() {
        val retrofit = RetrofitClient.instance
        myApi = retrofit.create(INodeJS::class.java)

        val storageManager = StorageManager(applicationContext)
        val cookies =  "connect.sid=" + storageManager.getData(Constants.COOKIE_STORAGE_KEY)

        var call = myApi.checkSession(cookies)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(all: Call<Void>, response: Response<Void>) {
                if  (response.code() == 200) {
                    SocketManager.resumeConnection(storageManager.getData(Constants.PHONE_STORAGE_KEY).toString())
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