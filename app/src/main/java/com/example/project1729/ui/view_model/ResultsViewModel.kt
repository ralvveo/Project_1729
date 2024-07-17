package com.example.project1729.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project1729.data.repository.CheckSaverRepository
import com.example.project1729.domain.model.Check
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ResultsViewModel:  ViewModel(), KoinComponent {

    private val state = MutableLiveData<List<Check>>()
    private val checkRepository: CheckSaverRepository by inject()

    fun getState(): LiveData<List<Check>> = state

    init{
        getChecks()
    }

    fun getChecks(){
        viewModelScope.launch {
            state.value = checkRepository.getChecksList()
        }

    }
}