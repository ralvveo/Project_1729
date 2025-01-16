package com.example.project1729.data.repository

import com.example.project1729.data.keys.RABKIN_KEYS.ALL_RABKIN_TESTS
import com.example.project1729.data.keys.RABKIN_KEYS.RABKIN_PROBLEM_TESTS
import com.example.project1729.data.keys.RABKIN_RESULTS.DEITERANOPY_ANSWERS
import com.example.project1729.data.keys.RABKIN_RESULTS.MAX_RIGHT_ANSWERS
import com.example.project1729.data.keys.RABKIN_RESULTS.MAX_WRONG_ANSWERS
import com.example.project1729.data.keys.RABKIN_RESULTS.PROTANOPY_ANSWERS
import com.example.project1729.data.keys.RABKIN_RESULTS.RIGHT_ANSWERS
import com.example.project1729.data.keys.RABKIN_RESULTS.SHOW_ONLY_FROM_BASE
import com.example.project1729.data.keys.RABKIN_RESULTS.USED_TESTS
import com.example.project1729.data.keys.RABKIN_RESULTS.WRONG_ANSWERS
import com.example.project1729.domain.model.RabkinTest
import com.example.project1729.domain.repository.RabkinRepository
import kotlin.random.Random

class RabkinRepositoryImpl() : RabkinRepository {
    override fun getRandomRabkinTest(): RabkinTest {
        var rabkinTest: RabkinTest
        if (SHOW_ONLY_FROM_BASE) {
            do {
                rabkinTest = RABKIN_PROBLEM_TESTS.random()
            } while (rabkinTest in USED_TESTS)
        } else {
            val randomNumber = Random.nextInt(1, 4)
            if (randomNumber == 1) {
                do {
                    rabkinTest = RABKIN_PROBLEM_TESTS.random()
                } while (rabkinTest in USED_TESTS)
            } else {
                do {
                    rabkinTest = ALL_RABKIN_TESTS.random()
                } while (rabkinTest in USED_TESTS)
            }

        }

        USED_TESTS.add(rabkinTest)
        return rabkinTest
    }

    override fun doAnswer(rabkinTest: RabkinTest, userAnswer: String): Boolean {
        val answer = rabkinTest.answer
        var dopAnswer = ""
        if (answer.contains(" ")){
            val dopAnswerArray = answer.split(" ")
            dopAnswer = dopAnswerArray[1] + " " + dopAnswerArray [0]

        }
        if ((userAnswer.trimEnd() == rabkinTest.answer) or (userAnswer.trimEnd() == dopAnswer)){
            RIGHT_ANSWERS += 1
        }
        else{
            if (rabkinTest in RABKIN_PROBLEM_TESTS){
                SHOW_ONLY_FROM_BASE = true
                if (userAnswer == rabkinTest.deiteranopy)
                    DEITERANOPY_ANSWERS += 1
                if (userAnswer == rabkinTest.protanopy)
                    PROTANOPY_ANSWERS += 1
                else
                    WRONG_ANSWERS += 1
            }
            else{
                WRONG_ANSWERS += 1
            }
        }

        if ((RIGHT_ANSWERS == MAX_RIGHT_ANSWERS) or (WRONG_ANSWERS == MAX_WRONG_ANSWERS) or (DEITERANOPY_ANSWERS == MAX_WRONG_ANSWERS)  or (PROTANOPY_ANSWERS == MAX_WRONG_ANSWERS)){
            return true
        }
        else{
            return false
        }
    }
}