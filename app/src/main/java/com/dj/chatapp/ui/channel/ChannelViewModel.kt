package com.dj.chatapp.ui.channel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User
import javax.inject.Inject

@HiltViewModel
class ChannelViewModel @Inject
constructor(
    private val client: ChatClient,
) : ViewModel(){

    fun logOut() {
        client.disconnect()
    }

    fun getUser(): User? {
        return client.getCurrentUser()
    }
}