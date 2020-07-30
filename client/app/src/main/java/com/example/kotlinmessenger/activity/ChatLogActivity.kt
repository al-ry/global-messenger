package com.example.kotlinmessenger.activity

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinmessenger.MyApplication
import com.example.kotlinmessenger.R
import com.example.kotlinmessenger.retrofit.INodeJS
import com.example.kotlinmessenger.storage.Constants
import com.example.kotlinmessenger.storage.Message
import com.example.kotlinmessenger.storage.StorageManager
import com.example.kotlinmessenger.storage.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.text.SimpleDateFormat
import java.util.*


class ChatLogActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        val storageManager = StorageManager(applicationContext);
        val user = intent.getParcelableExtra<User>("user")
        supportActionBar?.title = user?.name + " | " + user?.telephone



        val adapter = GroupAdapter<GroupieViewHolder>()

        recycler_view_chat_log.adapter = adapter
        
        recycler_view_chat_log.adapter = LoadingHistory(
            storageManager.getData(Constants.phoneStorageKey).toString(),
            user.telephone)
        val sendButton: Button = findViewById(R.id.send_button_chat_log)


        val phoneNumber = storageManager.getData(Constants.phoneStorageKey)
        sendButton.setOnClickListener {
            val messageText = message_field_chat_log.text.toString()
            if (messageText.isNotEmpty() && messageText.isNotBlank()) {
                adapter.add(
                    ChatToItem(
                        messageText
                    )
                )

                if (phoneNumber != user.telephone)
                {
                    Toast.makeText(this, phoneNumber, Toast.LENGTH_SHORT).show()
                    recycler_view_chat_log.adapter = adapter
                    message_field_chat_log.text.clear()
                    addNewDialog(phoneNumber.toString(), user.telephone)
                    val dateFormat = SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss", Locale.getDefault())

                    val date = Date()
                    val currentDate = dateFormat.format(date)
                    MyApplication.m_socket.emit(
                        "send_message",
                        phoneNumber,
                        user.telephone,
                        messageText,
                        currentDate
                    )
                }
            }
        }

        MyApplication.m_socket.on("new_message") {
            args->
            runOnUiThread {
                adapter.add(ChatFromItem(args[0].toString()))
            }

        }
    }

    private fun LoadingHistory(senderPhone : String, receiverPhone: String): GroupAdapter<GroupieViewHolder>? {
        val adapter = GroupAdapter<GroupieViewHolder>()

        val myApi = createRetrofitClientToParseJSON()
        var call = myApi.getHistory(senderPhone, receiverPhone)

        call.enqueue(object : Callback<List<Message>> {
            override fun onResponse(all: Call<List<Message>>, response: Response<List<Message>>) {
                val body = response.body()
                if (body != null) {
                    for (item in body) {
                        if (item.senderPhone == senderPhone)
                            adapter.add(ChatToItem(item.text))
                        else
                            adapter.add(ChatFromItem(item.text))
                    }
                }
                showMessages(body)
            }

            override fun onFailure(call: Call<List<Message>>, t: Throwable) {
            }
        })

        return adapter
    }

    private fun showMessages(body: List<Message>?) {

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

    private fun createRetrofitClientToParseJSON(): INodeJS {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.url)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(INodeJS::class.java)
    }

    private fun createRetrofitClient(): INodeJS {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.url)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        return retrofit.create(INodeJS::class.java)
    }
}

class ChatFromItem(val message: String) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        if (message.isNotEmpty())
        viewHolder.itemView.message_from_text.text = message
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
