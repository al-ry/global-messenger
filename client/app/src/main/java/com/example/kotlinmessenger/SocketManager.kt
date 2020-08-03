package com.example.kotlinmessenger

import com.example.kotlinmessenger.storage.Constants
import io.socket.client.IO
import io.socket.client.Socket

object SocketManager {
    lateinit var m_socket: Socket

    fun getSocket(): Socket {
        return m_socket
    }

    fun setSocket(socket : Socket) {
        m_socket = socket
    }

    fun setConnection(cookie: String, phone: String) {
        m_socket = IO.socket(Constants.URL)
        m_socket.connect()
        m_socket.emit(Constants.USER_CONNECTION_SOCKET_EVENT, phone, cookie)
    }

    fun resumeConnection(phone: String) {
        m_socket = IO.socket(Constants.URL)
        m_socket.connect()
        m_socket.emit(Constants.RESUME_CONNECTION_SOCKET_EVENT, phone)
    }
}