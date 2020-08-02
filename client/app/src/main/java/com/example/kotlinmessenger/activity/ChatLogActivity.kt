package com.example.kotlinmessenger.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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
import kotlin.concurrent.schedule

class ChatLogActivity : AppCompatActivity() {
    private lateinit var companion : User

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        val storageManager = StorageManager(applicationContext);
        companion = intent.getParcelableExtra<User>("user")

        supportActionBar?.title = companion.name

        val adapter = GroupAdapter<GroupieViewHolder>()
        recycler_view_chat_log.adapter = adapter
        val senderPhone = storageManager.getData(Constants.PHONE_STORAGE_KEY).toString()
        val receiverPhone = companion.telephone
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
                    recycler_view_chat_log.scrollToPosition(adapter.itemCount - 1)
                }
            }

            override fun onFailure(call: Call<List<Message>>, t: Throwable) {
            }
        })

        MyApplication.m_socket.on(Constants.USER_IS_TYPING_SOCKET_EVENT) {
            args->
            runOnUiThread {
                var isTypingText: TextView = findViewById(R.id.is_typing_text)
                if (args[0] == true) {
                    isTypingText.text = companion.name + " is typing.."
                } else {
                    isTypingText.text = ""
                }

            }

        }
        val sendButton: Button = findViewById(R.id.send_button_chat_log)
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
                    MyApplication.m_socket.emit(Constants.IS_TYPING_SOCKET_EVENT, false, companion.telephone)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                recycler_view_chat_log.scrollToPosition(adapter.itemCount - 1)
                MyApplication.m_socket.emit(Constants.IS_TYPING_SOCKET_EVENT, true, companion.telephone)
            }
        })

        chatLogMessageField.setOnClickListener{
            recycler_view_chat_log.scrollToPosition(adapter.itemCount - 1)
        }


        val phoneNumber = storageManager.getData(Constants.PHONE_STORAGE_KEY)
        sendButton.setOnClickListener {
            val messageText = message_field_chat_log.text.toString()
            if (messageText.isNotEmpty() && messageText.isNotBlank()) {
                adapter.add(ChatToItem(messageText))
                message_field_chat_log.text.clear()
                recycler_view_chat_log.scrollToPosition(adapter.itemCount - 1)

                addNewDialog(phoneNumber.toString(), companion.telephone)
                val dateFormat = SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault())

                val date = Date()
                val currentDate = dateFormat.format(date)
                MyApplication.m_socket.emit(
                    Constants.SEND_MESSAGE_SOCKET_EVENT,
                    phoneNumber,
                    companion.telephone,
                    messageText,
                    currentDate
                )

                MyApplication.m_socket.emit(Constants.IS_TYPING_SOCKET_EVENT, false, companion.telephone)
            }
        }

        MyApplication.m_socket.on(Constants.NEW_MESSAGE_SOCKET_EVENT) {
            args->
            runOnUiThread {
                if  (storageManager.getData(Constants.PHONE_STORAGE_KEY).toString() != companion.telephone) {
                    adapter.add(ChatFromItem(args[0].toString()))
                    recycler_view_chat_log.scrollToPosition(adapter.itemCount - 1)
                }
            }
        }
    }

//    private fun LoadingHistory(senderPhone : String, receiverPhone: String): GroupAdapter<GroupieViewHolder>? {
//        val adapter = GroupAdapter<GroupieViewHolder>()
//
//
//        return adapter
//    }

    private fun addNewDialog(fromNumber: String, toNumber: String) {
        val myApi = createRetrofitClientToParseJSON()

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
            .baseUrl(Constants.URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(INodeJS::class.java)
    }

    private fun createRetrofitClient(): INodeJS {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.URL)
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

