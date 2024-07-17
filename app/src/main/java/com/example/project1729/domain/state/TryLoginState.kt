package com.example.project1729.domain.state

sealed interface TryLoginState {
    object Loading: TryLoginState
    object Success: TryLoginState
    object Fail: TryLoginState
    object Default: TryLoginState
    data class Error(val errorMessage: String): TryLoginState
}