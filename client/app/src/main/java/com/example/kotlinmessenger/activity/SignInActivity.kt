package com.example.kotlinmessenger.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinmessenger.MyApplication
import com.example.kotlinmessenger.R
import com.example.kotlinmessenger.retrofit.INodeJS
import com.example.kotlinmessenger.storage.Constants
import com.example.kotlinmessenger.storage.CookieStorage
import com.example.kotlinmessenger.storage.StorageManager
import io.socket.client.IO
import kotlinx.android.synthetic.main.activity_sign_in.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class SignInActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val signInButton: Button = findViewById(R.id.sign_in_button)
        signInButton.setOnClickListener {
            val userInfo = listOf<String>(sign_in_phone_number_field.text.toString(),
                sign_in_password_field.text.toString())

            if (checkUserInfo(userInfo))
                signInUser(userInfo)
            else
                Toast.makeText(this, "Fields should not be empty", Toast.LENGTH_SHORT).show()
        }

        sign_up_link.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun createRetrofitClientToParseJSON(): INodeJS {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(INodeJS::class.java)
    }

    private fun signInUser(infoList: List<String>) {
        val myApi = createRetrofitClientToParseJSON()
        val storageManager = StorageManager(applicationContext);

        val call = myApi.logInUser(
            phone = infoList.first(),
            password = infoList.last()
        )

        call.enqueue(object : Callback<CookieStorage> {
            override fun onResponse(
                all: Call<CookieStorage>,
                response: Response<CookieStorage>
            ) {
                if (response.code() == 400)
                    Toast.makeText(
                        this@SignInActivity, "User not found",
                        Toast.LENGTH_LONG
                    ).show()
                else {
                    storageManager.putData(Constants.COOKIE_STORAGE_KEY, response.body()!!.cookie.toString());
                    storageManager.putData(Constants.PHONE_STORAGE_KEY, infoList.first());
                    setConnetcion(infoList.first())
                    intent = Intent(this@SignInActivity, LastMessagesActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<CookieStorage>, t: Throwable) {
                Toast.makeText(
                    this@SignInActivity, "There was an error with authorization",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun setConnetcion(phone: String) {
        val storageManager = StorageManager(applicationContext)
        MyApplication.m_socket = IO.socket(Constants.URL)
        MyApplication.m_socket.connect()
        MyApplication.m_socket.emit("user_connected", phone,
            storageManager.getData(Constants.COOKIE_STORAGE_KEY))
    }


    private fun checkUserInfo(infoList: List<String>) : Boolean{

        for (item in infoList) {
            if (item.isEmpty()) return false
        }
        return true
    }
}