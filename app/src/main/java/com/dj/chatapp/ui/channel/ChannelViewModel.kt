package com.dj.chatapp.ui.channel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.call.await
import io.getstream.chat.android.client.models.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChannelViewModel @Inject
constructor(
    private val client: ChatClient,
) : ViewModel() {
    private val _createChannelEvent = MutableSharedFlow<CreateChannelEvent>()
    val createChannelEvent = _createChannelEvent.asSharedFlow()

    fun createChannel(channelName: String) {
        val trimmedChannelName = channelName.trim()

        viewModelScope.launch {
            if (trimmedChannelName.isEmpty()) {
                _createChannelEvent.emit(CreateChannelEvent.Error(error = "Name is Empty"))
                return@launch
            }
            val result = client.channel(
                channelType = "messaging",
                channelId = UUID.randomUUID().toString()
            ).create(
                mapOf(
                    "name" to trimmedChannelName
                )
            ).await()
            if (result.isError) {
                _createChannelEvent.emit(CreateChannelEvent.Error(error = result.error().message
                    ?: "Unknown Error"))
                return@launch
            }
            _createChannelEvent.emit(CreateChannelEvent.Success)
        }
    }

    fun logOut() {
        client.disconnect()
    }

    fun getUser(): User? {
        return client.getCurrentUser()
    }
}

sealed class CreateChannelEvent {
    data class Error(val error: String) : CreateChannelEvent()
    object Success : CreateChannelEvent()
}