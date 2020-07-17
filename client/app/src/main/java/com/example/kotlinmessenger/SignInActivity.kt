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


class SignInActivity: AppCompatActivity()
{
    lateinit var myApi: INodeJS
    var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val signInButton: Button = findViewById(R.id.sign_up_button)
        val retrofit = RetrofitClient.instance
        myApi = retrofit.create(INodeJS::class.java)

        log_in_link.setOnClickListener{
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        signInButton.setOnClickListener{
            compositeDisposable.add(myApi.LogInUser("asdad", "asdasd")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{message ->
                    if (message.contains("encrypted_password"))
                        Toast.makeText(this , "Login success", Toast.LENGTH_LONG).show()
                })
        }

    }
}
