package com.example.kotlinmessenger
import io.reactivex.Observable
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.kotlinmessenger.retrofit.INodeJS
import com.example.kotlinmessenger.retrofit.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var myApi:INodeJS
    var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val signInButton: Button = findViewById(R.id.sign_in_button)
        val retrofit = RetrofitClient.instance
        myApi = retrofit.create(INodeJS::class.java)

        log_in_link.setOnClickListener{
            val intent = Intent(this, SignInActivity::class.java)
          startActivity(intent)
        }

        signInButton.setOnClickListener{
            val info = getUserInfo()
            compositeDisposable.add(myApi.LogInUser(info[0], info[1])
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{message ->
                    if (message.contains("encrypted_password"))
                        Toast.makeText(this , "Login success", Toast.LENGTH_LONG).show()
                    else
                        Toast.makeText(this , message, Toast.LENGTH_LONG).show()
                })
        }
    }

    private fun showUserData() {
        val info = getUserInfo()
        var result = String()

        if (info[0].isEmpty() || info[1].isEmpty() || info[2].isEmpty())
        {
            result = "You should fill all the fields"
        }
        else
        {
            result = info[0] + "/n" + info[1] + "/n" + info[2]
        }

        Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
    }

    private fun getUserInfo(): List<String> {
        val password = password_field.text.toString()
        val phoneNumber = phone_number_field.text.toString()

        return listOf(phoneNumber, password)
    }
}
