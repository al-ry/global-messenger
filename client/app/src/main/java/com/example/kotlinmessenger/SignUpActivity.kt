package com.example.kotlinmessenger
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.kotlinmessenger.storage.CookieStorage
import com.example.kotlinmessenger.retrofit.INodeJS
import com.example.kotlinmessenger.retrofit.RetrofitClient
import com.example.kotlinmessenger.storage.StorageManager
import com.example.kotlinmessenger.storage.User
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class SignUpActivity : AppCompatActivity()
{
    lateinit var myApi:INodeJS
    private lateinit var storageManager : StorageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val signUpButton: Button = findViewById(R.id.sign_up_button)
        signUpButton.setOnClickListener{
            val info = checkUserInfo(sign_up_username_field.text.toString(),
            sign_up_phone_number_field.text.toString(), sign_up_password_field.text.toString(),
                sign_up_password_repeat_field.text.toString())

            if (info.isNotEmpty())
            {
                registrate(info)
            }
        }
    }
    private fun createRetrofitClientToParseJSON(): INodeJS {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(INodeJS::class.java)
    }

    private fun registrate(userInfo : List<String>)
    {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val myApi = createRetrofitClientToParseJSON()
        storageManager = StorageManager(applicationContext)

        var call = myApi.registerUser(userInfo[0], userInfo[1], userInfo[2])

        call.enqueue(object : Callback<CookieStorage> {
            override fun onResponse(all: Call<CookieStorage>, response: Response<CookieStorage>)
            {
                if  (response.code() == 400)
                    Toast.makeText(this@SignUpActivity, "login is busy",
                        Toast.LENGTH_LONG).show()
                else
                {
                    storageManager.putData("cookies", response.body()!!.cookie.toString());
                    storageManager.putData("currentUserPhone", userInfo[1]);
                    startActivity(Intent(this@SignUpActivity, LastMessagesActivity::class.java))
                }
            }

            override fun onFailure(call: Call<CookieStorage>, t: Throwable) {
                Toast.makeText(this@SignUpActivity, "There was an error with registration",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun checkUserInfo(username: String, phone: String,
                              password: String, repeatPassword : String) : List<String>
    {
        if (phone.isEmpty() || username.isEmpty() || password.isEmpty())
        {
            Toast.makeText(this , "Fields should not be empty", Toast.LENGTH_LONG).show()
            return emptyList()
        }

        if (password != repeatPassword)
        {
            Toast.makeText(this , "Passwords does not match", Toast.LENGTH_LONG).show()
            return emptyList()
        }

        return listOf(username, phone, password)
    }
}
