package com.example.project1729.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project1729.domain.model.RabkinTest
import com.example.project1729.domain.model.SivtsevTest
import com.example.project1729.domain.repository.RabkinRepository

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RabkinTestViewModel: ViewModel(), KoinComponent {

    private val state = MutableLiveData<RabkinTest>()
    private val rabkinRepository: RabkinRepository by inject()

    init{
        state.value = RabkinTest("", "", "" ,"")
    }

    fun getLiveData(): LiveData<RabkinTest> = state

    fun changeText(newText: String){
        state.value = state.value?.copy(answer = newText)
    }

    fun getRandomRabkinTest(): RabkinTest{
        return rabkinRepository.getRandomRabkinTest()
    }

    fun getRandomSivtsevTest(): SivtsevTest{
        return rabkinRepository.getRandomSivtsevTest()
    }

    fun doAnswer(rabkinTest: RabkinTest, userAnswer: String): Boolean{
        return rabkinRepository.doAnswer(rabkinTest, userAnswer)
    }

    fun doAnswer(sivtsevTest: SivtsevTest, userAnswer: String): Boolean{
        return rabkinRepository.doAnswer(sivtsevTest, userAnswer)
    }
}