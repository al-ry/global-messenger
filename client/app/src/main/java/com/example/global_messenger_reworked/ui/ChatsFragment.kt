package com.example.global_messenger_reworked.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.global_messenger_reworked.R
import com.example.global_messenger_reworked.utils.Status
import com.example.global_messenger_reworked.viewModels.ChatsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatsFragment : Fragment() {

    private val viewModel: ChatsViewModel by viewModels()
    private lateinit var rvChats: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var chatsAdapter: ChatsAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(view)
        setupObserve()
    }

    private fun setupRecyclerView(view: View) {
        progressBar = view.findViewById(R.id.chat_list_loader)
        chatsAdapter = ChatsAdapter { item ->
            val bundle = bundleOf("friend_phone" to item.getChatInfo().friendPhone)
            findNavController().navigate(R.id.action_chatsFragment_to_chatFragment, bundle)
        }

        rvChats = view.findViewById(R.id.recycle_view_chats)
        rvChats.adapter = chatsAdapter
        rvChats.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvChats.setHasFixedSize(true)
    }

    private fun setupObserve() {
        viewModel.chats.observe(viewLifecycleOwner, Observer {
            when(it.status) {
                Status.SUCCESS -> {
                    rvChats.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    it.data.let {it ->
                        it.let {
                            chatsAdapter.setupPersonList(it!!);
                        }
                    }
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    rvChats.visibility = View.GONE
                }
                Status.ERROR -> {}
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chats, container, false)
    }
}