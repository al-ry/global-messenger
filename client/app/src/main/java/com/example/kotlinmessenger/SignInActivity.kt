package com.example.kotlinmessenger

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinmessenger.Response.CookieStorage
import com.example.kotlinmessenger.retrofit.CookiesManagement
import com.example.kotlinmessenger.retrofit.INodeJS
import com.example.kotlinmessenger.retrofit.RetrofitClient
import kotlinx.android.synthetic.main.activity_sign_in.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class SignInActivity: AppCompatActivity()
{
    private lateinit var myApi: INodeJS
    private lateinit var cookiesManagement : CookiesManagement

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val signInButton: Button = findViewById(R.id.sign_in_button)
        cookiesManagement = CookiesManagement(applicationContext)

        myApi = retrofit.create(INodeJS::class.java)
        signInButton.setOnClickListener {
            var call = myApi.LogInUser(sign_in_phone_field.text.toString(),
                sign_in_password_field.text.toString())

            call.enqueue(object : Callback<CookieStorage> {
                override fun onResponse(all: Call<CookieStorage>, response: Response<CookieStorage>)
                {
                    if  (response.code() == 400)
                    {
                        Toast.makeText(this@SignInActivity, "User not found",
                            Toast.LENGTH_LONG).show()
                    }
                    else
                    {
                        cookiesManagement.PutCookie(response.body()!!.cookie.toString());
                        startActivity(Intent(this@SignInActivity, LastMessagesActivity::class.java))
                    }
                }

                override fun onFailure(call: Call<CookieStorage>, t: Throwable) {
                    Toast.makeText(this@SignInActivity, "There was an error with authorization",
                        Toast.LENGTH_SHORT).show()
                }
            })

        }

        sign_up_link.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun CheckUserInfo(phone: String, password: String) : List<String>
    {
        if (phone.isEmpty() || password.isEmpty())
        {
            Toast.makeText(this , "Fields should not be empty", Toast.LENGTH_LONG).show()
            return emptyList()
        }

        return listOf(phone, password)
    }
}
