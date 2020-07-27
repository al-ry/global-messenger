package com.example.kotlinmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.kotlinmessenger.retrofit.INodeJS
import com.example.kotlinmessenger.storage.StorageManager
import com.example.kotlinmessenger.storage.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_to_row.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


class ChatLogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        val storageManager = StorageManager(applicationContext);
        val user = intent.getParcelableExtra<User>("user")
        supportActionBar?.title = user?.name + " | " + user?.telephone

        val adapter = GroupAdapter<GroupieViewHolder>()

        recycler_view_chat_log.adapter = adapter
        val sendButton : Button = findViewById(R.id.send_button_chat_log)

        sendButton.setOnClickListener{
            val messageText = message_field_chat_log.text.toString()
            if (messageText.isNotEmpty() && messageText.isNotBlank())
            {
                adapter.add(ChatToItem(messageText))
                val phoneNumber = storageManager.getData("currentUserPhone")
                Toast.makeText(this, phoneNumber, Toast.LENGTH_SHORT).show()
                recycler_view_chat_log.adapter = adapter
                message_field_chat_log.text.clear()
                addNewDialog(phoneNumber.toString(), user.telephone)
            }
        }
    }

    private fun addNewDialog(fromNumber: String, toNumber: String) {
        val myApi = createRetrofitClient()

        var call = myApi.addChat(fromNumber, toNumber)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(all: Call<Void>, response: Response<Void>) {
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
            }
        })

    }
    private fun createRetrofitClient(): INodeJS {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        return retrofit.create(INodeJS::class.java)
    }
}

class ChatFromItem(val message: String) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        if (message.isNotEmpty())
        viewHolder.itemView.message_to_text.text = message
    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}

class ChatToItem(val message : String) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        if (message.isNotEmpty())
        viewHolder.itemView.message_to_text.text = message
    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}
