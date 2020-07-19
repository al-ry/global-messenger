package com.example.kotlinmessenger

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinmessenger.retrofit.INodeJS
import com.example.kotlinmessenger.retrofit.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_sign_in.*


class SignInActivity: AppCompatActivity()
{
   lateinit var myApi:INodeJS
   var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val retrofit = RetrofitClient.instance
        val signInButton: Button = findViewById(R.id.sign_in_button)

        myApi = retrofit.create(INodeJS::class.java)
        signInButton.setOnClickListener {
            val info = CheckUserInfo(sign_in_phone_field.text.toString(),
                sign_in_password_field.text.toString())

            if (info.isNotEmpty())
            {
                LogIn(info)
            }
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

    private fun LogIn(userInfo : List<String>)
    {
        compositeDisposable.add(myApi.LogInUser(userInfo[0], userInfo[1])
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{message ->
                if (message == "user successfully registrated") {
                    Toast.makeText(this, "Login success", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, LatestMessagesActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                else
                    Toast.makeText(this , message, Toast.LENGTH_LONG).show()
            })
    }
}
