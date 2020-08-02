package com.example.kotlinmessenger.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinmessenger.SocketManager
import com.example.kotlinmessenger.R
import com.example.kotlinmessenger.holder.ChatFromItemHolder
import com.example.kotlinmessenger.holder.ChatToItemHolder
import com.example.kotlinmessenger.retrofit.INodeJS
import com.example.kotlinmessenger.retrofit.RetrofitClient
import com.example.kotlinmessenger.storage.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.schedule

class ChatLogActivity : AppCompatActivity() {
    private lateinit var companion : User
    private var adapter = GroupAdapter<GroupieViewHolder>()
    private lateinit var myApi: INodeJS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        val retrofit = RetrofitClient.instance
        myApi = retrofit.create(INodeJS::class.java)

        val storageManager = StorageManager(applicationContext);
        companion = intent.getParcelableExtra("user")
        supportActionBar?.title = companion.name
        recycler_view_chat_log.adapter = adapter

        showChatHistory()
        detectTyping()
        sendMessage()

        SocketManager.getSocket().on(Constants.USER_IS_TYPING_SOCKET_EVENT) {
            args->
            runOnUiThread {
                var isTypingText: TextView = findViewById(R.id.is_typing_text)
                if (args[0] == true)
                    isTypingText.text = companion.name + " is typing.."
                else
                    isTypingText.text = ""
            }
        }

        SocketManager.getSocket().on(Constants.NEW_MESSAGE_SOCKET_EVENT) {
            args->
            runOnUiThread {
                if  (storageManager.getData(Constants.PHONE_STORAGE_KEY).toString() != companion.telephone) {
                    adapter.add(
                        ChatFromItemHolder(
                            args[0].toString()
                        )
                    )
                    recycler_view_chat_log.scrollToPosition(adapter.itemCount - 1)
                }
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?) : Boolean
    {
        menuInflater.inflate(R.menu.chat_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.show_user_phone_icon -> {
                if (supportActionBar?.title == companion.name)
                    supportActionBar?.title = companion.telephone
                else
                    supportActionBar?.title = companion.name
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showChatHistory() {
        val storageManager = StorageManager(applicationContext);
        val senderPhone = storageManager.getData(Constants.PHONE_STORAGE_KEY).toString()
        val receiverPhone = companion.telephone
        var call = myApi.getHistory(senderPhone, receiverPhone)

        call.enqueue(object : Callback<List<Message>> {
            override fun onResponse(all: Call<List<Message>>, response: Response<List<Message>>) {
                val body = response.body()
                if (body != null) {
                    for (item in body) {
                        if (item.senderPhone == senderPhone)
                            adapter.add(ChatToItemHolder(item.text))
                        else
                            adapter.add(ChatFromItemHolder(item.text))
                    }
                    recycler_view_chat_log.scrollToPosition(adapter.itemCount - 1)
                }
            }

            override fun onFailure(call: Call<List<Message>>, t: Throwable) {
            }
        })
    }

    private fun sendMessage() {
        val storageManager = StorageManager(applicationContext);
        val phoneNumber = storageManager.getData(Constants.PHONE_STORAGE_KEY)
        val sendButton: Button = findViewById(R.id.send_button_chat_log)
        sendButton.setOnClickListener {
            val messageText = message_field_chat_log.text.toString()
            if (messageText.isNotEmpty() && messageText.isNotBlank()) {
                adapter.add(ChatToItemHolder(messageText))
                message_field_chat_log.text.clear()
                recycler_view_chat_log.scrollToPosition(adapter.itemCount - 1)
                addNewDialog(phoneNumber.toString(), companion.telephone)

                SocketManager.getSocket().emit(
                    Constants.SEND_MESSAGE_SOCKET_EVENT,
                    phoneNumber,
                    companion.telephone,
                    messageText,
                    getCurrentDate()
                )

                SocketManager.getSocket().emit(Constants.IS_TYPING_SOCKET_EVENT, false, companion.telephone)
            }
        }
    }

    private fun getCurrentDate() : String
    {
        val dateFormat = SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    private fun detectTyping() {
        val storageManager = StorageManager(applicationContext);
        val chatLogMessageField : EditText = findViewById(R.id.message_field_chat_log)
        chatLogMessageField.addTextChangedListener(object: TextWatcher
        {
            var timer = Timer()
            override fun afterTextChanged(s: Editable?) {
                timer.cancel()
                val sleep = when(s?.length) {
                    1 -> 1000L
                    2,3 -> 700L
                    4,5 -> 500L
                    else -> 300L
                }

                timer = Timer()
                timer.schedule(sleep) {
                    SocketManager.getSocket().emit(Constants.IS_TYPING_SOCKET_EVENT, false, companion.telephone)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                recycler_view_chat_log.scrollToPosition(adapter.itemCount - 1)
                if (companion.telephone != storageManager.getData(Constants.PHONE_STORAGE_KEY).toString())
                    SocketManager.getSocket().emit(Constants.IS_TYPING_SOCKET_EVENT, true, companion.telephone)
            }
        })
    }

    private fun addNewDialog(fromNumber: String, toNumber: String) {
        var call = myApi.addChat(fromNumber, toNumber)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(all: Call<Void>, response: Response<Void>) {
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@ChatLogActivity, "Error with adding chat", Toast.LENGTH_SHORT).show()
            }
        })
    }
}





