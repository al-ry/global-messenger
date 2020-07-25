package com.example.kotlinmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.kotlinmessenger.Response.User
import com.example.kotlinmessenger.retrofit.INodeJS
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

    fun FindUser(phone : String){

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        myApi = retrofit.create(INodeJS::class.java)

        var call = myApi.FindUser(phone).also {
            it.enqueue(object : Callback<List<User>> {
                override fun onResponse(all: Call<List<User>>, response: Response<List<User>>) {
                    val body = response.body()

                    if (body != null) {
                            if (body.isNotEmpty())
                                this@FindUserActivity.RenderSearchResult(body)
                            else
                                Toast.makeText(this@FindUserActivity, "There are no matches", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    Toast.makeText(this@FindUserActivity, "Fail with search", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun RenderSearchResult(userList : List<User>) {
        val adapter = GroupAdapter<GroupieViewHolder>()
        recycle_view_found_user.adapter = adapter
//        adapter.add(UserHolder(userList.first()))
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


