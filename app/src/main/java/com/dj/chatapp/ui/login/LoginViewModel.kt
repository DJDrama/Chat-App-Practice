package com.dj.chatapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dj.chatapp.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.call.await
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject
constructor(
    private val client: ChatClient,
) : ViewModel() {
    private val _loginEvent = MutableSharedFlow<LoginEvent>()
    val loginEvent = _loginEvent.asSharedFlow()

    private fun isValidUserName(userName: String) = userName.length >= Constants.MIN_USERNAME_LENGTH

    fun connectUser(userName: String) {
        val trimmedUserName = userName.trim()
        viewModelScope.launch {
            if (isValidUserName(trimmedUserName)) {
                val result = client.connectGuestUser(
                    userId = trimmedUserName,
                    username = trimmedUserName
                ).await() // Call

                if (result.isError) {
                    _loginEvent.emit(LoginEvent.ErrorLogin(result.error().message
                        ?: "Unknown Error"))
                    return@launch
                }
                _loginEvent.emit(LoginEvent.Success)
            }else{
                _loginEvent.emit(LoginEvent.ErrorInputTooShort)
            }
        }
    }
}

sealed class LoginEvent {
    object ErrorInputTooShort : LoginEvent()
    data class ErrorLogin(val error: String) : LoginEvent()
    object Success : LoginEvent()
}