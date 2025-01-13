package com.example.project1729.domain.repository

import com.example.project1729.domain.model.UserLogin
import com.example.project1729.domain.model.UserRegister
import com.example.project1729.domain.state.TryLoginState
import com.example.project1729.domain.state.TryRegisterState
import kotlinx.coroutines.flow.Flow

interface RetrofitSheetsRepository {

    fun getRegisteredUsers(user: UserLogin): Flow<TryLoginState>
    fun registerNewUser(user: UserRegister): Flow<TryRegisterState>
}