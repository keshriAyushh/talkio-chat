package com.ayush.talkio.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayush.talkio.data.model.ChatRequest
import com.ayush.talkio.data.repository.RequestsRepository
import com.ayush.talkio.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequestsViewModel @Inject constructor(
    private val requestsRepository: RequestsRepository
) : ViewModel() {

    private val _allRequests = MutableStateFlow<Response<List<ChatRequest>>>(Response.None)
    val allRequest = _allRequests.asStateFlow()

    private val _acceptRequest = MutableStateFlow<Response<Boolean>>(Response.None)
    val acceptRequest = _acceptRequest.asStateFlow()

    fun rejectRequest(request: ChatRequest) {
        viewModelScope.launch {
            requestsRepository.rejectRequest(request)
        }
    }

    fun acceptRequest(request: ChatRequest) {
        viewModelScope.launch {
            requestsRepository.acceptRequest(request)
                .collect {
                    _acceptRequest.value = it
                }
        }
    }

    fun getAllRequests() {
        viewModelScope.launch {
            requestsRepository.getAllRequests().collect {
                _allRequests.value = it
            }
        }
    }

}