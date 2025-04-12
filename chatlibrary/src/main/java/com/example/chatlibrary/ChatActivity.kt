package com.example.chatlibrary

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import java.util.concurrent.TimeUnit

internal class ChatActivity : AppCompatActivity() {

    private lateinit var client: OkHttpClient
    private var webSocket: WebSocket? = null
    private lateinit var chatAdapter: ChatAdapter
    private val messagesList = mutableListOf<ChatMessage>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var messageInput: EditText
    private lateinit var sendButton: Button

    private val webSocketUrl = "wss://echo.websocket.org/"
    private val NORMAL_CLOSURE_STATUS = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        recyclerView = findViewById(R.id.recyclerViewChat)
        messageInput = findViewById(R.id.editTextMessage)
        sendButton = findViewById(R.id.buttonSend)

        setupRecyclerView()
        setupWebSocket()
        setupSendButton()
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter(messagesList)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity).apply {
                stackFromEnd = true
            }
            adapter = chatAdapter
        }
    }

    private fun setupWebSocket() {
        client = OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .pingInterval(30, TimeUnit.SECONDS)
            .build()

        val request = Request.Builder().url(webSocketUrl).build()
        val listener = ChatWebSocketListener { message, isReceivedFromServer ->
            runOnUiThread {
                addMessageToList(message, !isReceivedFromServer)
            }
        }
        webSocket = client.newWebSocket(request, listener)
    }


    private fun setupSendButton() {
        sendButton.setOnClickListener {
            val messageText = messageInput.text.toString().trim()
            if (messageText.isNotEmpty()) {
                webSocket?.send(messageText)
                addMessageToList(messageText, true)
                messageInput.setText("")
            }
        }
    }

    private fun addMessageToList(text: String, isSent: Boolean) {
        val message = ChatMessage(text, isSent)
        messagesList.add(message)
        chatAdapter.notifyItemInserted(messagesList.size - 1)
        recyclerView.scrollToPosition(messagesList.size - 1)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ChatActivity", "onDestroy: Closing WebSocket")
        webSocket?.close(NORMAL_CLOSURE_STATUS, "Activity Destroyed")
        client.dispatcher.executorService.shutdown()
    }
}