package com.example.kotlinmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.kotlinmessenger.retrofit.INodeJS
import com.example.kotlinmessenger.storage.StorageManager
import com.example.kotlinmessenger.storage.User
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class LoadingActivity : AppCompatActivity() {

    lateinit var myApi: INodeJS
    private lateinit var storageManager : StorageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)
        storageManager = StorageManager(applicationContext)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        myApi = retrofit.create(INodeJS::class.java)

        val cookies =  "connect.sid=" + storageManager.getData("cookies")

        var call = myApi.checkSession(cookies)

        call.enqueue(object : Callback<User> {
            override fun onResponse(all: Call<User>, response: Response<User>)
            {
                if  (response.code() == 200) {
                    val body = response.body()
                    val intent = Intent(this@LoadingActivity, LastMessagesActivity::class.java)
                    intent.putExtra("currentUser", body)
                    startActivity(intent)
                }
                else
                    startActivity(Intent(this@LoadingActivity, SignInActivity::class.java))
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@LoadingActivity, "There was an error with authorization",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }
}