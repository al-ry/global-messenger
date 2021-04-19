package com.example.global_messenger_reworked.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.global_messenger_reworked.R
import com.example.global_messenger_reworked.data.models.Chat
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ChatsAdapter(private val listener: (ChatViewHolder) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mChatList = ArrayList<Chat>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.chat_row, parent, false)

        return ChatViewHolder(itemView = itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ChatViewHolder) {
            holder.bind(mChatList[position])
            holder.itemView.setOnClickListener{ listener(holder) }
        }
    }

    fun setupPersonList(friendsList : List<Chat>) {
        mChatList.addAll(friendsList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mChatList.count();
    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private lateinit var chatInfo : Chat
        private var chatUsername: TextView = itemView.findViewById(R.id.chat_username)
        private var chatLastMessage : TextView = itemView.findViewById(R.id.chat_last_message)
        private var mCivAvatar: CircleImageView = itemView.findViewById(R.id.chat_civ_avatar)

        fun getChatInfo() : Chat {
            return chatInfo
        }

        fun bind(chat: Chat) {
            chatInfo = chat
            chatUsername.text = chatInfo.friendName
            chatLastMessage.text = chatInfo.message
            Picasso.get().load("https://miro.medium.com/max/1838/1*mk1-6aYaf_Bes1E3Imhc0A.jpeg").into(mCivAvatar)
        }
    }
}
