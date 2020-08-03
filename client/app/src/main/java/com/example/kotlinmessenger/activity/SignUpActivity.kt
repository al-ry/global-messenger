package com.example.kotlinmessenger.activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.kotlinmessenger.R
import com.example.kotlinmessenger.SocketManager
import com.example.kotlinmessenger.storage.CookieStorage
import com.example.kotlinmessenger.retrofit.INodeJS
import com.example.kotlinmessenger.retrofit.RetrofitClient
import com.example.kotlinmessenger.storage.Constants
import com.example.kotlinmessenger.storage.StorageManager
import com.example.kotlinmessenger.storage.UserInfo
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class SignUpActivity : AppCompatActivity() {
    lateinit var myApi: INodeJS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val signUpButton: Button = findViewById(R.id.sign_up_button)
        signUpButton.setOnClickListener{
            val info = UserInfo(
                sign_up_phone_number_field.text.toString(),
                sign_up_password_field.text.toString(),
                sign_up_username_field.text.toString())

            if (checkUserInfo(info, sign_up_password_repeat_field.text.toString()) != null) {
                registrate(info)
            }
        }
    }

    private fun registrate(info : UserInfo) {
        val retrofit = RetrofitClient.instance
        myApi = retrofit.create(INodeJS::class.java)

        var call = myApi.registerUser(info.username, info.phone, info.password)

        call.enqueue(object : Callback<CookieStorage> {
            override fun onResponse(all: Call<CookieStorage>, response: Response<CookieStorage>)
            {
                if  (response.code() == 400)
                    Toast.makeText(this@SignUpActivity, "login is busy",
                        Toast.LENGTH_LONG).show()
                else {
                    putUserInfo(response.body()!!.cookie.toString(), info.phone)
                    SocketManager.setConnection(response.body()!!.cookie.toString(), info.phone)
                    intent = Intent(this@SignUpActivity, LastMessagesActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<CookieStorage>, t: Throwable) {
                Toast.makeText(this@SignUpActivity, "There was an error with registration",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun putUserInfo(cookie: String, phone: String) {
        val storageManager = StorageManager(applicationContext);
        storageManager.putData(Constants.COOKIE_STORAGE_KEY, cookie);
        storageManager.putData(Constants.PHONE_STORAGE_KEY, phone);
    }

    private fun checkUserInfo(info: UserInfo, repeatPassword : String) : UserInfo? {
        if (info.phone.isEmpty() || info.username.isEmpty() || info.password.isEmpty()) {
            Toast.makeText(this , "Fields should not be empty", Toast.LENGTH_LONG).show()
            return null
        }

        if (info.phone.length != 11) {
            Toast.makeText(this , "Phone should contain 11 digits, like 89065547839", Toast.LENGTH_LONG).show()
            return null
        }

        if (info.username.length > 20) {
            Toast.makeText(this , "Username cant contain more than 20 symbols", Toast.LENGTH_LONG).show()
            return null
        }

        if (info.password != repeatPassword) {
            Toast.makeText(this , "Passwords do not match", Toast.LENGTH_LONG).show()
            return null
        }

        return info
    }
}
