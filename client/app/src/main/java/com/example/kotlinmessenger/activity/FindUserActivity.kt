package com.example.kotlinmessenger.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import com.example.kotlinmessenger.R
import com.example.kotlinmessenger.storage.User
import com.example.kotlinmessenger.retrofit.INodeJS
import com.example.kotlinmessenger.retrofit.RetrofitClient
import com.example.kotlinmessenger.storage.UserHolder
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_find_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FindUserActivity : AppCompatActivity() {
    private lateinit var myApi: INodeJS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_user)

        val retrofit = RetrofitClient.instance
        myApi = retrofit.create(INodeJS::class.java)
        searchByTyping()
    }

    private fun searchByTyping() {
        val userPhoneEditText: EditText = findViewById(R.id.user_phone_for_search)
        var phoneForSearch : String

        userPhoneEditText.addTextChangedListener(object: TextWatcher
        {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                phoneForSearch = user_phone_for_search.text.toString()

                if (phoneForSearch.isNotEmpty())
                    findUser(user_phone_for_search.text.toString())
            }
        })
    }

    fun findUser(phone: String) {
        var call = myApi.findUser(phone)
        call.enqueue(object : Callback<List<User>> {
            override fun onResponse(all: Call<List<User>>, response: Response<List<User>>) {
                val body = response.body()
                if (body != null) {
                    this@FindUserActivity.showSearchResult(body)
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Toast.makeText(this@FindUserActivity, "Fail with search", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun showSearchResult(userList: List<User>) {
        val adapter = GroupAdapter<GroupieViewHolder>()
        recycle_view_found_user.adapter = adapter

        for (user in userList) {
            adapter.add(UserHolder(user))
        }

        adapter.setOnItemClickListener{ item, view ->
            val userItem = item as UserHolder
            val intent = Intent(view.context, ChatLogActivity::class.java)
            intent.putExtra("user", userItem.user)
            startActivity(intent)
        }
    }
}



