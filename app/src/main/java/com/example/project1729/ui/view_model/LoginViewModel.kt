package com.example.project1729.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project1729.domain.model.UserLogin
import com.example.project1729.domain.repository.NetworkRepository
import com.example.project1729.domain.state.TryLoginState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LoginViewModel: ViewModel(), KoinComponent {

    private val state = MutableLiveData<UserLogin>()
    private val loginState = MutableLiveData<TryLoginState>()

    private val retrofitRepository: NetworkRepository by inject()
    init{
        state.value = UserLogin("", "")
    }
    fun getLoginLiveData(): LiveData<UserLogin> = state

    fun getLoginStateLiveData(): LiveData<TryLoginState> = loginState

    fun changeLoginText(newText: String){
        state.value = state.value?.copy(login = newText)
    }

    fun changePasswordText(newText: String){
        state.value = state.value?.copy(password = newText)
    }

    fun tryToLogin(){
        viewModelScope.launch{
            retrofitRepository
                .getRegisteredUsers(state.value?: UserLogin("", ""))
                .collect{tryLoginState ->
                    loginState.postValue(tryLoginState)
                    delay(1000L)
                    loginState.postValue(TryLoginState.Default)
                }
        }
    }

}