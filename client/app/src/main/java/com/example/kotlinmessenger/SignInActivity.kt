package com.example.kotlinmessenger

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinmessenger.storage.CookieStorage
import com.example.kotlinmessenger.retrofit.INodeJS
import com.example.kotlinmessenger.storage.StorageManager
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
            val userInfo = listOf<String>(sign_in_phone_field.text.toString(),
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

    private fun createRetrofitClient(): INodeJS {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(INodeJS::class.java)
    }

    private fun signInUser(infoList: List<String>) {
        val myApi = createRetrofitClient()
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
                    storageManager.putData("cookies", response.body()!!.cookie.toString());
                    storageManager.putData("phone", sign_in_phone_field.text.toString());
                    startActivity(Intent(this@SignInActivity, LastMessagesActivity::class.java))
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


    private fun checkUserInfo(infoList: List<String>) : Boolean{

        for (item in infoList) {
            if (item.isEmpty()) return false
        }
        return true
    }
}