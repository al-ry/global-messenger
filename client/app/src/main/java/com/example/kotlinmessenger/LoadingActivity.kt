package com.example.kotlinmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.kotlinmessenger.retrofit.CookiesManagement
import com.example.kotlinmessenger.retrofit.INodeJS
import com.example.kotlinmessenger.retrofit.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
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

        compositeDisposable.add(myApi.CheckSession(cookies)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{message ->
                if (message == "success") {
                    Toast.makeText(this , "Все по кайфу", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, LastMessagesActivity::class.java))
                }
                else
                {
                    Toast.makeText(this , "Вы обосрались, Станислав, помойте жопу", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, SignInActivity::class.java))
                }
            })
    }
}