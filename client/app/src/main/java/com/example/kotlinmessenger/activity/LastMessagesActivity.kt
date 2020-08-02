package com.example.kotlinmessenger.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinmessenger.SocketManager
import com.example.kotlinmessenger.R
import com.example.kotlinmessenger.holder.MessageHolder
import com.example.kotlinmessenger.retrofit.INodeJS
import com.example.kotlinmessenger.retrofit.RetrofitClient
import com.example.kotlinmessenger.storage.*
import com.example.kotlinmessenger.storage.Constants.LOG_OUT_USER_SOCKET_EVENT
import com.example.kotlinmessenger.storage.Constants.SHOW_LAST_MESSAGE_SOCKET_EVENT
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_last_messages.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class LastMessagesActivity : AppCompatActivity() {
    private lateinit var myApi : INodeJS
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_last_messages)
        val retrofit = RetrofitClient.instance
        myApi = retrofit.create(INodeJS::class.java)

        formChatPage()

        SocketManager.m_socket.on(LOG_OUT_USER_SOCKET_EVENT)
        {
            unlogUser()
        }

        SocketManager.m_socket.on(SHOW_LAST_MESSAGE_SOCKET_EVENT) { args ->
            runOnUiThread {
                val storageManager = StorageManager(applicationContext);
                addNewDialog(storageManager.getData(Constants.PHONE_STORAGE_KEY).toString(), args[0].toString())

            }
        }
    }

    private fun addNewDialog(fromNumber: String, toNumber: String) {
        var call = myApi.addChat(fromNumber, toNumber)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(all: Call<Void>, response: Response<Void>) {
                formChatPage()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
            }
        })
    }


    fun formChatPage() {
        val storageManager = StorageManager(applicationContext);
        val userPhone = storageManager.getData(Constants.PHONE_STORAGE_KEY)
        var call = myApi.getChats(userPhone.toString())

        call.enqueue(object : Callback<List<Chat>> {
            override fun onResponse(all: Call<List<Chat>>, response: Response<List<Chat>>)
            {
                if  (response.code() == 200) {
                    val body = response.body()
                    if (body != null) {
                        showChatPage(body)
                    }
                }
            }

            override fun onFailure(call: Call<List<Chat>>, t: Throwable) {
                Toast.makeText(this@LastMessagesActivity, "There was an error with authorization",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun showChatPage(userList: List<Chat>) {
        val storageManager = StorageManager(applicationContext);
        val userPhone = storageManager.getData(Constants.PHONE_STORAGE_KEY).toString()
        val adapter = GroupAdapter<GroupieViewHolder>()
        recycle_view_messages.adapter = adapter
        adapter.clear()

        for (chat in userList) {
            adapter.add(
                MessageHolder(
                    chat,
                    userPhone
                )
            )
        }

        adapter.setOnItemLongClickListener { item, view ->
            val userItem = item as MessageHolder
            createPopUpMenu(userPhone, item.chat.friendPhone, adapter, userItem)
            true
        }

        adapter.setOnItemClickListener{ item, view ->
            val userItem = item as MessageHolder
            val intent = Intent(view.context, ChatLogActivity::class.java)
            intent.putExtra("user", User(item.chat.friendPhone, item.chat.friendName))
            startActivity(intent)
        }
    }

    private fun createPopUpMenu(
        userPhone: String?,
        telephone: String,
        adapter: GroupAdapter<GroupieViewHolder>,
        userItem: MessageHolder
    ) {
        AlertDialog.Builder(this@LastMessagesActivity)
            .setTitle("Attention")
            .setMessage("Chat will be removed. Are you sure?")
            .setCancelable(false)
            .setPositiveButton("Yes") {
                    dialog, which -> adapter.remove(userItem)
                deleteChat(userPhone.toString(), telephone)
            }
            .setNegativeButton("Back") {
                    dialog, which ->
            }
            .show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.menu_new_message -> {
                startActivity(Intent(this@LastMessagesActivity,
                        FindUserActivity::class.java))
            }
            R.id.menu_sign_out -> {
                signOut()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?) : Boolean
    {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun deleteChat(userPhone : String, friendPhone: String) {
        val call = myApi.deleteChat(userPhone, friendPhone)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(all: Call<Void>, response: Response<Void>)
            {
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@LastMessagesActivity, "There was an error with authorization",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun unlogUser()
    {
        val storageManager = StorageManager(applicationContext)

        val cookies =  "connect.sid=" + storageManager.getData(Constants.COOKIE_STORAGE_KEY)

        var call = myApi.logOut(cookies)

        call.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@LastMessagesActivity,
                    "Problems with logging out", Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                var phone = storageManager.getData(Constants.PHONE_STORAGE_KEY).toString()
                storageManager.deleteData(Constants.COOKIE_STORAGE_KEY)
                startActivity(Intent(this@LastMessagesActivity,
                    SignInActivity::class.java))
            }
        })
    }

    private fun signOut()
    {
        val storageManager = StorageManager(applicationContext)
        val cookies =  "connect.sid=" + storageManager.getData(Constants.COOKIE_STORAGE_KEY)

        var call = myApi.logOut(cookies)

        call.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@LastMessagesActivity,
                    "Problems with logging out", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                var phone = storageManager.getData(Constants.PHONE_STORAGE_KEY).toString()
                SocketManager.m_socket.emit("disconnection" , phone)
                SocketManager.m_socket.disconnect()
                storageManager.deleteData(Constants.COOKIE_STORAGE_KEY)
                intent = Intent(this@LastMessagesActivity,
                    SignInActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        })
    }
}

