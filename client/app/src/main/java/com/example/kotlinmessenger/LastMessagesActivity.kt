package com.example.kotlinmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.kotlinmessenger.retrofit.CookiesManagement
import com.example.kotlinmessenger.retrofit.INodeJS
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class LastMessagesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_last_messages)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.menu_new_message -> {
                startActivity(
                    Intent(this@LastMessagesActivity,
                        FindUserActivity::class.java))
            }
            R.id.menu_sign_out -> {
                SignOut()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu?) : Boolean
    {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun SignOut()
    {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        val myApi = retrofit.create(INodeJS::class.java)
        val cookiesManagement = CookiesManagement(applicationContext)
        val cookies =  "connect.sid=" + cookiesManagement.GetCookie()

        var call = myApi.LogOut(cookies)

        call.enqueue(object : Callback<Void> {

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@LastMessagesActivity,
                    "Problems with logging out", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                cookiesManagement.DeleteCookie()
                Toast.makeText(this@LastMessagesActivity, "You have just signed out",
                    Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@LastMessagesActivity,
                    SignInActivity::class.java))
            }

        })
    }
}
