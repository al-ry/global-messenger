package com.example.kotlinmessenger.holder

import com.example.kotlinmessenger.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_from_row.view.*

class ChatFromItemHolder(val message: String) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        if (message.isNotEmpty())
            viewHolder.itemView.message_from_text.text = message
    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}