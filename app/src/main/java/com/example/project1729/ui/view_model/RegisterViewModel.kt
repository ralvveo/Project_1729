package com.example.project1729.ui.view_model


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project1729.domain.model.UserRegister
import com.example.project1729.domain.repository.NetworkRepository
import com.example.project1729.domain.state.TryRegisterState
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RegisterViewModel: ViewModel(), KoinComponent {

    private val state = MutableLiveData<UserRegister>()
    private val registerState = MutableLiveData<TryRegisterState>()
    private val retrofitRepository: NetworkRepository by inject()

    init{
        state.value = UserRegister("", "", "", "", "")

    }
    fun getRegisterLiveData(): LiveData<UserRegister> = state
    fun getRegisterStateLiveData(): LiveData<TryRegisterState> = registerState

    fun changeFioText(newText: String){
        state.value = state.value?.copy(fio = newText)
    }

    fun changeYearText(newText: String){
        state.value = state.value?.copy(year = newText)
    }

    fun changeDiagnozText(newText: String){
        state.value = state.value?.copy(diagnoz = newText)
    }

    fun changeLoginText(newText: String){
        state.value = state.value?.copy(login = newText)
    }

    fun changePasswordText(newText: String){
        state.value = state.value?.copy(password = newText)
    }

    fun tryToRegister(){
        viewModelScope.launch{
            retrofitRepository
                .registerNewUser(state.value?:UserRegister("", "", "", "", ""))
                .collect{tryRegisterState ->
                    registerState.postValue(tryRegisterState)

                }
        }
    }
}