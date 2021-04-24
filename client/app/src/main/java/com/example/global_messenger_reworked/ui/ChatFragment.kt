package com.example.global_messenger_reworked.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.global_messenger_reworked.R
import com.example.global_messenger_reworked.ui.adapters.ChatsAdapter
import com.example.global_messenger_reworked.ui.adapters.MessageHistoryAdapter
import com.example.global_messenger_reworked.utils.Status
import com.example.global_messenger_reworked.viewModels.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : Fragment() {
    private val viewModel: ChatViewModel by viewModels()
    private lateinit var progressBar: ProgressBar
    private lateinit var rvChats: RecyclerView
    private lateinit var messageHistoryAdapter: MessageHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
        setupObserve()
        viewModel.getMessageHistory(arguments?.getString("friend_phone")!!)
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

    private fun setupViews(view: View) {
        progressBar = view.findViewById(R.id.chat_loader)

        messageHistoryAdapter = MessageHistoryAdapter("89021089168")

        rvChats = view.findViewById(R.id.recycler_view_message_history)
        rvChats.adapter = messageHistoryAdapter
        rvChats.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvChats.setHasFixedSize(true)
    }

    private fun setupObserve() {
        viewModel.messageHistory.observe(viewLifecycleOwner, {
            when(it.status) {
                Status.SUCCESS -> {
                    rvChats.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    it.data.let {it ->
                        it.let {
                            messageHistoryAdapter.setupMessageHistory(it!!);
                            rvChats.scrollToPosition(messageHistoryAdapter.itemCount - 1);
                        }
                    }
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