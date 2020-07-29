package com.example.kotlinmessenger

import android.app.Application
import io.socket.client.Socket

object MyApplication : Application() {
    private lateinit var m_socket: Socket

    fun getSocket(): Socket {
        return m_socket
    }

    fun setSocket(socket : Socket) {
        m_socket = socket
    }

}