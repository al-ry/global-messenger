package com.example.kotlinmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import com.example.kotlinmessenger.storage.User
import com.example.kotlinmessenger.retrofit.INodeJS
import com.example.kotlinmessenger.storage.UserHolder
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_find_user.*
import kotlinx.android.synthetic.main.found_user_row.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class FindUserActivity : AppCompatActivity() {
    private lateinit var myApi: INodeJS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_user)

        val userPhoneEditText: EditText = findViewById(R.id.user_phone_for_search)

        userPhoneEditText.addTextChangedListener(object: TextWatcher
        {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                findUser(user_phone_for_search.text.toString())
            }
        })
    }

    fun findUser(phone: String){
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        myApi = retrofit.create(INodeJS::class.java)

        myApi.findUser(phone).also {
            it.enqueue(object : Callback<List<User>> {
                override fun onResponse(all: Call<List<User>>, response: Response<List<User>>) {
                    val body = response.body()

                    if (body != null) {
                        if (body.isNotEmpty())
                            this@FindUserActivity.renderSearchResult(body)
                        else
                            this@FindUserActivity.renderSearchResult(emptyList())
                    }
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    Toast.makeText(this@FindUserActivity, "Fail with search", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun renderSearchResult(userList: List<User>) {
        val adapter = GroupAdapter<GroupieViewHolder>()
        recycle_view_found_user.adapter = adapter

        for (user in userList) {
            adapter.add(UserHolder(user))
        }

        adapter.setOnItemClickListener{ item, view ->
            val userItem = item as UserHolder
            adapter.remove(userItem)
            val intent = Intent(view.context, ChatLogActivity::class.java)
            intent.putExtra("user", userItem.user)
            startActivity(intent)
        }
    }
}



