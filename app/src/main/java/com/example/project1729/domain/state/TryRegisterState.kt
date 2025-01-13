package com.example.project1729.domain.state

sealed interface TryRegisterState {
    data class Error(val errorMessage: String): TryRegisterState
    object Success: TryRegisterState
    object Loading: TryRegisterState
}