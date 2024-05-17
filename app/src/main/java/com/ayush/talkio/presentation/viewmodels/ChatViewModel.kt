package com.ayush.talkio.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayush.talkio.data.model.Message
import com.ayush.talkio.data.model.User
import com.ayush.talkio.data.repository.ChatRepository
import com.ayush.talkio.utils.HandleOnlineActivity
import com.ayush.talkio.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _allMessagesState = MutableStateFlow<Response<List<Message>>>(Response.None)
    val allMessagesState = _allMessagesState.asStateFlow()

    private val _userInfo = MutableStateFlow<Response<User>>(Response.None)
    val userInfo = _userInfo.asStateFlow()

    private val _isUserOnline = MutableLiveData<Boolean>()
    val isUserOnline: LiveData<Boolean>
        get() = _isUserOnline

    fun getAllMessages(receiverId: String) {
        viewModelScope.launch {
            chatRepository.getMessages(receiverId).collect {
                _allMessagesState.value = it
            }
        }
    }

    fun startListeningForUserOnlineStatus(userId: String) {
        HandleOnlineActivity().listenForUserOnlineStatus(userId) { isOnline ->
            _isUserOnline.postValue(isOnline)
        }
    }

    fun sendMessage(message: Message) {
        viewModelScope.launch {
            chatRepository.sendMessage(message)
        }
    }

    fun getReceiverInfo(receiverId: String) {
        viewModelScope.launch {
            chatRepository.getUser(receiverId).collect {
                _userInfo.value = it
            }
        }
    }

}