package com.example.kotlinmessenger.storage

import android.graphics.Color
import com.example.kotlinmessenger.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.found_chat_row.view.*

class  MessageHolder(val chat: Chat, val userPhone: String) : Item<GroupieViewHolder>()
{
    override fun getLayout(): Int {
        return R.layout.found_chat_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.chat_username.text = chat.friendName
        if (chat.senderPhone == userPhone)
            viewHolder.itemView.chat_last_message.text = "You: " + chat.message
        else
            viewHolder.itemView.chat_last_message.text = chat.message
    }
}