package com.example.chatlibrary

import android.util.Log
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

internal class ChatWebSocketListener(private val messageCallback: (String, Boolean) -> Unit) : WebSocketListener() {


    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.d("ChatWebSocket", "Connection Opened")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.d("ChatWebSocket", "Receiving text: $text")
        messageCallback(text, true)
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        messageCallback("Received binary data: ${bytes.hex()}", true)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("ChatWebSocket", "Connection Closing")
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("ChatWebSocket", "Connection Closed")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.d("ChatWebSocket", "Connection error: {${t.message}}")
    }
}