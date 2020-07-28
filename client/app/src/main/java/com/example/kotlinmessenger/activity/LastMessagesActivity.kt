package com.example.kotlinmessenger.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.system.Os.socket
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinmessenger.R
import com.example.kotlinmessenger.retrofit.INodeJS
import com.example.kotlinmessenger.storage.StorageManager
import com.example.kotlinmessenger.storage.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.android.synthetic.main.activity_last_messages.*
import kotlinx.android.synthetic.main.found_chat_row.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.lang.Exception


class LastMessagesActivity : AppCompatActivity() {

    lateinit var socket: Socket
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_last_messages)
    }

    override fun onStart() {
        super.onStart()
        formChatPage()
        startConnection()
    }


    private fun startConnection() {
        try {
            socket = IO.socket("http://10.0.2.2:3000/")
            socket.on(Socket.EVENT_CONNECT) {
                socket.connect()
            }
        } catch (ex:Exception){
            Toast.makeText(this, "Problem", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createRetrofitClientToParseJSON(): INodeJS {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(INodeJS::class.java)
    }

    fun formChatPage()
    {
        val storageManager = StorageManager(applicationContext);
        val userPhone = storageManager.getData("currentUserPhone")
        val myApi = createRetrofitClientToParseJSON()

        var call = myApi.getChats(userPhone.toString())

        call.enqueue(object : Callback<List<User>> {
            override fun onResponse(all: Call<List<User>>, response: Response<List<User>>)
            {
                if  (response.code() == 200) {
                    val body = response.body()
                    if (body != null) {
                        showChatPage(body)
                    }
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Toast.makeText(this@LastMessagesActivity, "There was an error with authorization",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun showChatPage(userList: List<User>) {
        val storageManager = StorageManager(applicationContext);
        val userPhone = storageManager.getData("currentUserPhone")

        val adapter = GroupAdapter<GroupieViewHolder>()
        recycle_view_messages.adapter = adapter

        for (user in userList) {
            adapter.add(MessageHolder(user))
        }

        adapter.setOnItemLongClickListener { item, view ->
            val userItem = item as MessageHolder
            createPopUpMenu(userPhone, item.user.telephone, adapter, userItem)
            true
        }

        adapter.setOnItemClickListener{ item, view ->
            val userItem = item as MessageHolder
            val intent = Intent(view.context, ChatLogActivity::class.java)
            intent.putExtra("user", userItem.user)
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
            .setMessage("Message history will be deleted. Are you sure?")
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

    fun deleteChat(userPhone : String, friendPhone: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        val myApi = retrofit.create(INodeJS::class.java)
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

    fun SignOut()
    {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        val myApi = retrofit.create(INodeJS::class.java)
        val storageManager = StorageManager(applicationContext)

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