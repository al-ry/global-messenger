package com.example.kotlinmessenger
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.kotlinmessenger.retrofit.INodeJS
import com.example.kotlinmessenger.retrofit.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class SignUpActivity : AppCompatActivity()
{
    lateinit var myApi:INodeJS
    var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val signUpButton: Button = findViewById(R.id.sign_up_button)
        val retrofit = RetrofitClient.instance

        myApi = retrofit.create(INodeJS::class.java)

        signUpButton.setOnClickListener{
            val info = CheckUserInfo(sign_up_username_field.text.toString(),
            sign_up_phone_field.text.toString(), sign_up_password_field.text.toString(),
                sign_up_password_repeat_field.text.toString())

            if (info.isNotEmpty())
            {
                Registrate(info)
            }
        }
    }

    private fun CheckUserInfo(username: String, phone: String,
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

    private fun Registrate(userInfo : List<String>)
    {
        compositeDisposable.add(myApi.RegisterUser(userInfo[0], userInfo[1], userInfo[2])
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{
                    message ->
                if (message == "You are successfully registrated")
                {
                    Toast.makeText(this, "Successed registration", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, SignInActivity::class.java)
                    startActivity(intent)
                }
                else
                    Toast.makeText(this ,  message, Toast.LENGTH_LONG).show()
            })
    }
}
