package com.example.global_messenger_reworked.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.global_messenger_reworked.R
import com.example.global_messenger_reworked.utils.Status
import com.example.global_messenger_reworked.viewModels.ChatViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : Fragment() {
    private val viewModel: ChatViewModel by viewModels()
    private lateinit var progressBar: ProgressBar
    private lateinit var rvChats: RecyclerView
    private lateinit var chatsAdapter: ChatsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //view.findViewById<TextView>(R.id.chat_fragment_text).text = arguments?.getString("friend_phone")
        setupRecyclerView(view)
        setupObserve()


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).hideBottomNavigation()
    }

    override fun onDetach() {
        (activity as MainActivity).showBottomNavigation()
        super.onDetach()
    }

    private fun setupRecyclerView(view: View) {
        progressBar = view.findViewById(R.id.chat_loader)
        chatsAdapter = ChatsAdapter { item ->
            val bundle = bundleOf("friend_phone" to item.getChatInfo().friendPhone)
            findNavController().navigate(R.id.action_chatsFragment_to_chatFragment, bundle)
        }

        rvChats = view.findViewById(R.id.recycler_view_chat_history)
        rvChats.adapter = chatsAdapter
        rvChats.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvChats.setHasFixedSize(true)
    }

    private fun setupObserve() {
        viewModel.messageHistory.observe(viewLifecycleOwner, {
            when(it.status) {
                Status.SUCCESS -> {
                    println(it.data)
                }

                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                }

                Status.ERROR -> {
                    println(it.message)
                }
            }
        })
    }



}