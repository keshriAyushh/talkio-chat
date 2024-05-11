package com.ayush.talkio.utils

sealed class Response <out T>{
    data object Loading: Response<Nothing>()
    data object None: Response<Nothing>()
    data class Error(val error: String): Response<Nothing>()
    data class Success<T>(val data: T): Response<T>()
}