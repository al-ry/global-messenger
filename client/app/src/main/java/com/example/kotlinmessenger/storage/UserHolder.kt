package com.example.kotlinmessenger.storage

import com.example.kotlinmessenger.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.found_user_row.view.*

class  UserHolder(val user: User) : Item<GroupieViewHolder>()
{
    override fun getLayout(): Int {
        return R.layout.found_user_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.found_username.text = user.name
        viewHolder.itemView.found_phone.text = user.telephone
    }

}