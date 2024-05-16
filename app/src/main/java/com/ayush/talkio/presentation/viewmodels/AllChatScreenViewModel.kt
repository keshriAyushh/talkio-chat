package com.ayush.talkio.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayush.talkio.data.model.Chat
import com.ayush.talkio.data.repository.AllChatsRepository
import com.ayush.talkio.data.repository.AuthRepository
import com.ayush.talkio.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllChatScreenViewModel @Inject constructor(
    private val allChatsRepository: AllChatsRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _allChatsEvent = MutableStateFlow<Response<List<Chat>>>(Response.None)
    val allChatsEvent = _allChatsEvent.asStateFlow()

    private val _chatRequestEvent = MutableStateFlow<Response<Boolean>>(Response.None)
    val chatRequestEvent = _chatRequestEvent.asStateFlow()

    fun getAllChats() {
        viewModelScope.launch {
            allChatsRepository.getAllChatRooms()
                .collect {
                    _allChatsEvent.value = it
                }
        }
    }

    fun sendChatRequest(to: String) {
        viewModelScope.launch {
            allChatsRepository.sendChatRequest(to)
                .collect {
                    _chatRequestEvent.value = it
                }

        }
    }

    fun getCurrentUserId() = authRepository.getCurrentUserId()

}