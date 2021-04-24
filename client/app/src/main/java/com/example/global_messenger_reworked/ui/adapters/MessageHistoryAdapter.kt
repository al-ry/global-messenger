package com.example.global_messenger_reworked.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.global_messenger_reworked.R
import com.example.global_messenger_reworked.data.models.Chat
import com.example.global_messenger_reworked.data.models.Message
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class MessageHistoryAdapter(private val userPhone: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var messageList = ArrayList<Message>()

    companion object {
        private const val MESSAGE_FROM = 0
        private const val MESSAGE_TO = 1
    }

    fun setupMessageHistory(messageHistory : List<Message>) {
        messageList.addAll(messageHistory)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        val message = messageList[position]
        return if (message.sender == userPhone) MESSAGE_TO else MESSAGE_FROM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            MESSAGE_FROM -> {
                val itemView = layoutInflater.inflate(R.layout.message_from_row, parent, false)
                MessageFromViewHolder(itemView = itemView)
            }

            MESSAGE_TO -> {
                val itemView = layoutInflater.inflate(R.layout.message_to_row, parent, false)
                MessageToViewHolder(itemView = itemView)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MessageToViewHolder -> {holder.bind(messageList[position])}
            is MessageFromViewHolder -> {holder.bind(messageList[position])}
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }
}


class MessageFromViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
    private lateinit var message: Message
    private val messageCiv: CircleImageView = itemView.findViewById(R.id.message_from_civ)
    private val messageText: TextView = itemView.findViewById(R.id.message_from_text)

    fun bind(message: Message) {
        messageText.text = message.message
        Picasso.get().load("https://miro.medium.com/max/1838/1*mk1-6aYaf_Bes1E3Imhc0A.jpeg").into(messageCiv)
    }

}


class MessageToViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private lateinit var message : Message
    private val messageCiv: CircleImageView = itemView.findViewById(R.id.message_to_civ)
    private val messageText: TextView = itemView.findViewById(R.id.message_to_text)

    fun bind(message: Message) {
        messageText.text = message.message
        Picasso.get().load("https://miro.medium.com/max/1838/1*mk1-6aYaf_Bes1E3Imhc0A.jpeg").into(messageCiv)
    }

}