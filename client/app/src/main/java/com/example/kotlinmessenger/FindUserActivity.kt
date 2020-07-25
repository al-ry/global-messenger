package com.example.kotlinmessenger

import android.content.Intent
import android.hardware.usb.UsbRequest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinmessenger.Response.CookieStorage
import com.example.kotlinmessenger.Response.User
import com.example.kotlinmessenger.Response.UsersList
import com.example.kotlinmessenger.retrofit.INodeJS
import com.google.gson.Gson
import com.nineoldandroids.view.ViewHelper
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_find_user.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.found_user_row.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class FindUserActivity : AppCompatActivity() {
    private lateinit var myApi: INodeJS

    fun FindUser(phone : String){

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        myApi = retrofit.create(INodeJS::class.java)

        var call = myApi.FindUser(phone).also {
            it.enqueue(object : Callback<User> {
                override fun onResponse(all: Call<User>, response: Response<User>) {
                    val body = response.body()

                    if (body != null) {
                        this@FindUserActivity.RenderSearchResult(body)
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    println("123123123123123123123123123")
                }
            })
        }
    }

    fun RenderSearchResult(user : User) {
        val adapter = GroupAdapter<GroupieViewHolder>()

        adapter.add(UserHolder(user))
        recycle_view_found_user.adapter = adapter
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_user)

        val findUserButton: Button = findViewById(R.id.find_user_button)
        findUserButton.setOnClickListener {
            FindUser(user_phone_for_search.text.toString())
        }
    }
}

class  UserHolder(val user: User) : Item<GroupieViewHolder>()
{
    override fun getLayout(): Int {
        return R.layout.found_user_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.found_username.text = user.name
        viewHolder.itemView.found_phone.text = user.telephone
    }

}


