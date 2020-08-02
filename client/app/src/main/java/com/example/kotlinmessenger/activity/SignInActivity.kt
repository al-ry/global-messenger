package com.example.kotlinmessenger.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinmessenger.SocketManager
import com.example.kotlinmessenger.R
import com.example.kotlinmessenger.retrofit.INodeJS
import com.example.kotlinmessenger.retrofit.RetrofitClient
import com.example.kotlinmessenger.storage.Constants
import com.example.kotlinmessenger.storage.CookieStorage
import com.example.kotlinmessenger.storage.StorageManager
import kotlinx.android.synthetic.main.activity_sign_in.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInActivity: AppCompatActivity() {
    private lateinit var myApi : INodeJS

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val signInButton: Button = findViewById(R.id.sign_in_button)
        signInButton.setOnClickListener {
            val userInfo = listOf(sign_in_phone_number_field.text.toString(),
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

    private fun signInUser(infoList: List<String>) {
        val retrofit = RetrofitClient.instance
        myApi = retrofit.create(INodeJS::class.java)

        val call = myApi.logInUser(infoList.first(), infoList.last())

        call.enqueue(object : Callback<CookieStorage> {
            override fun onResponse(all: Call<CookieStorage>, response: Response<CookieStorage>) {
                if (response.code() == 400)
                    Toast.makeText(this@SignInActivity,
                        "User not found", Toast.LENGTH_LONG).show()
                else {
                    putUserInfo(response.body()!!.cookie.toString(), infoList.first())
                    SocketManager.setConnection(response.body()!!.cookie.toString(), infoList.first())
                    intent = Intent(this@SignInActivity, LastMessagesActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<CookieStorage>, t: Throwable) {
                Toast.makeText(
                    this@SignInActivity, "There was an error with authorization",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun putUserInfo(cookie: String, phone: String)
    {
        val storageManager = StorageManager(applicationContext);
        storageManager.putData(Constants.COOKIE_STORAGE_KEY, cookie);
        storageManager.putData(Constants.PHONE_STORAGE_KEY, phone);
    }

    private fun checkUserInfo(infoList: List<String>) : Boolean{

        for (item in infoList) {
            if (item.isEmpty()) return false
        }

        return true
    }
}