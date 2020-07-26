package com.example.kotlinmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.kotlinmessenger.Response.CookieStorage
import com.example.kotlinmessenger.retrofit.CookiesManagement
import com.example.kotlinmessenger.retrofit.INodeJS
import com.example.kotlinmessenger.retrofit.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_sign_in.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class LoadingActivity : AppCompatActivity() {

    lateinit var myApi: INodeJS
    var compositeDisposable = CompositeDisposable()
    private lateinit var cookiesManagement : CookiesManagement

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)
        cookiesManagement = CookiesManagement(applicationContext)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        myApi = retrofit.create(INodeJS::class.java)

        val cookies =  "connect.sid=" + cookiesManagement.GetCookie()

        var call = myApi.CheckSession(cookies)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(all: Call<Void>, response: Response<Void>)
            {
                if  (response.code() == 200)
                    startActivity(Intent(this@LoadingActivity, LastMessagesActivity::class.java))
                else
                    startActivity(Intent(this@LoadingActivity, SignInActivity::class.java))
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@LoadingActivity, "There was an error with authorization",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }
}