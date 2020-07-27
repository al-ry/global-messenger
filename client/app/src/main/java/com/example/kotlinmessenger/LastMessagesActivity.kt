package com.example.kotlinmessenger

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinmessenger.retrofit.INodeJS
import com.example.kotlinmessenger.storage.StorageManager
import com.example.kotlinmessenger.storage.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_last_messages.*
import kotlinx.android.synthetic.main.found_chat_row.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


class LastMessagesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_last_messages)
    }

    override fun onStart() {
        super.onStart()

        val storageManager = StorageManager(applicationContext);

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val myApi = retrofit.create(INodeJS::class.java)
        val userPhone = storageManager.getData("currentUserPhone")

        var call = myApi.getChats(userPhone.toString())

        call.enqueue(object : Callback<List<User>> {
            override fun onResponse(all: Call<List<User>>, response: Response<List<User>>)
            {
                if  (response.code() == 200) {
                    val body = response.body()
                    if (body != null) {
                        renderSearchResult(body)
                    }
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Toast.makeText(this@LastMessagesActivity, "There was an error with authorization",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun renderSearchResult(userList: List<User>) {
        val adapter = GroupAdapter<GroupieViewHolder>()
        recycle_view_messages.adapter = adapter

        for (user in userList) {
            adapter.add(MessageHolder(user))
        }

        adapter.setOnItemLongClickListener { item, view ->
            val userItem = item as MessageHolder

            AlertDialog.Builder(this@LastMessagesActivity)
                .setTitle("Attention")
                .setMessage("Message history will be deleted. Are you sure?")
                .setCancelable(false)
                .setPositiveButton("Yes") {
                        dialog, which -> adapter.remove(userItem)
                }
                .setNegativeButton("Back") {
                        dialog, which ->
                }
                .show()
            true
        }

        adapter.setOnItemClickListener{ item, view ->
            val userItem = item as MessageHolder
            adapter.remove(userItem)
            val intent = Intent(view.context, ChatLogActivity::class.java)
            intent.putExtra("user", userItem.user)
            startActivity(intent)
        }


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

    fun deleteChat() {

    }

    fun SignOut()
    {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        val myApi = retrofit.create(INodeJS::class.java)
        val storageManager =
            StorageManager(
                applicationContext
            )
        val cookies =  "connect.sid=" + storageManager.getData("cookies")

        var call = myApi.logOut(cookies)

        call.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@LastMessagesActivity,
                    "Problems with logging out", Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                storageManager.deleteData("cookies")
                storageManager.deleteData("phone")
                Toast.makeText(this@LastMessagesActivity, "You have just signed out",
                    Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@LastMessagesActivity,
                    SignInActivity::class.java))
            }
        })


    }
}

class  MessageHolder(val user: User) : Item<GroupieViewHolder>()
{
    override fun getLayout(): Int {
        return R.layout.found_chat_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.char_username.text = user.name
    }

}