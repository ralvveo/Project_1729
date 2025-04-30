package com.example.project1729.data.repository

import android.app.Application
import android.content.Context
import android.util.Log
import com.example.project1729.data.db.Database
import com.example.project1729.data.keys.RABKIN_KEYS.ALL_RABKIN_TESTS
import com.example.project1729.data.keys.RABKIN_KEYS.RABKIN_PROBLEM_TESTS
import com.example.project1729.data.keys.RABKIN_RESULTS.RABKIN_DEITERANOPY_ANSWERS
import com.example.project1729.data.keys.RABKIN_RESULTS.RABKIN_MAX_RIGHT_ANSWERS
import com.example.project1729.data.keys.RABKIN_RESULTS.RABKIN_MAX_WRONG_ANSWERS
import com.example.project1729.data.keys.RABKIN_RESULTS.RABKIN_PROTANOPY_ANSWERS
import com.example.project1729.data.keys.RABKIN_RESULTS.RABKIN_RIGHT_ANSWERS
import com.example.project1729.data.keys.RABKIN_RESULTS.RABKIN_SHOW_ONLY_FROM_BASE
import com.example.project1729.data.keys.RABKIN_RESULTS.RABKIN_USED_TESTS
import com.example.project1729.data.keys.RABKIN_RESULTS.RABKIN_WRONG_ANSWERS
import com.example.project1729.data.keys.SIVTSEV_KEYS.ALL_SIVTSEV_TESTS
import com.example.project1729.data.keys.SIVTSEV_RESULTS.SIVTSEV_CURRENT_LEVEL
import com.example.project1729.data.keys.SIVTSEV_RESULTS.SIVTSEV_CURRENT_LEVEL_WRONG
import com.example.project1729.data.keys.SIVTSEV_RESULTS.SIVTSEV_CURRENT_TEST
import com.example.project1729.data.keys.SIVTSEV_RESULTS.SIVTSEV_MAX_LEVEL
import com.example.project1729.data.keys.SIVTSEV_RESULTS.SIVTSEV_NUMBER_TO_SHOW_FROM_LEVEL
import com.example.project1729.data.keys.SIVTSEV_RESULTS.SIVTSEV_RESULT
import com.example.project1729.data.model.MeasurementRequest
import com.example.project1729.data.network.ServerApi
import com.example.project1729.domain.converter.Converter
import com.example.project1729.domain.model.RabkinTest
import com.example.project1729.domain.model.SivtsevTest
import com.example.project1729.domain.model.Test
import com.example.project1729.domain.repository.RabkinRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.random.Random

class RabkinRepositoryImpl(private val database: Database, val context: Context, val serverApi: ServerApi) : RabkinRepository {
    override fun getRandomRabkinTest(): RabkinTest {
        var rabkinTest: RabkinTest
        if (RABKIN_SHOW_ONLY_FROM_BASE) {
            var saveFromCrash = 0
            do {
                saveFromCrash += 1
                rabkinTest = RABKIN_PROBLEM_TESTS.random()
            } while ((rabkinTest in RABKIN_USED_TESTS) and (saveFromCrash < 30))
            if (saveFromCrash == 100){
                rabkinTest = RABKIN_PROBLEM_TESTS.random()
            }
        } else {
            val randomNumber = Random.nextInt(1, 4)
            if (randomNumber == 1) {
                do {
                    rabkinTest = RABKIN_PROBLEM_TESTS.random()
                } while (rabkinTest in RABKIN_USED_TESTS)
            } else {
                do {
                    rabkinTest = ALL_RABKIN_TESTS.random()
                } while (rabkinTest in RABKIN_USED_TESTS)
            }

        }

        RABKIN_USED_TESTS.add(rabkinTest)
        return rabkinTest
    }

    override fun getRandomSivtsevTest(): SivtsevTest {
        val currentLevel = SIVTSEV_CURRENT_LEVEL
        val sivtsevList = ALL_SIVTSEV_TESTS
        val currentLevelList = sivtsevList.filter { sivtsevTest -> (sivtsevTest.level == currentLevel.toString() && sivtsevTest != SIVTSEV_CURRENT_TEST)}
        val result = currentLevelList.random()
        SIVTSEV_CURRENT_TEST = result
        return result
    }

    override fun doAnswer(rabkinTest: RabkinTest, userAnswer: String): Boolean {
        val answer = rabkinTest.answer
        var dopAnswer = ""
        if (answer.contains(" ")){
            val dopAnswerArray = answer.split(" ")
            dopAnswer = dopAnswerArray[1] + " " + dopAnswerArray [0]

        }
        if ((userAnswer.trimEnd() == rabkinTest.answer) or (userAnswer.trimEnd() == dopAnswer)){
            RABKIN_RIGHT_ANSWERS += 1
        }
        else{
            if (rabkinTest in RABKIN_PROBLEM_TESTS){
                RABKIN_SHOW_ONLY_FROM_BASE = true
                if (userAnswer == rabkinTest.deiteranopy)
                    RABKIN_DEITERANOPY_ANSWERS += 1
                if (userAnswer == rabkinTest.protanopy)
                    RABKIN_PROTANOPY_ANSWERS += 1
                else
                    RABKIN_WRONG_ANSWERS += 1
            }
            else{
                RABKIN_WRONG_ANSWERS += 1
            }
        }


        Log.d("RABKIN_RIGHT", RABKIN_RIGHT_ANSWERS.toString())
        Log.d("RABKIN_WRONG", RABKIN_WRONG_ANSWERS.toString())
        Log.d("RABKIN_DEITER", RABKIN_DEITERANOPY_ANSWERS.toString())
        Log.d("RABKIN_PROTA", RABKIN_PROTANOPY_ANSWERS.toString())

        if (RABKIN_RIGHT_ANSWERS == RABKIN_MAX_RIGHT_ANSWERS){
            saveMeasure("Rabkin", "ok")
            return true
        }
        else if (RABKIN_WRONG_ANSWERS == RABKIN_MAX_WRONG_ANSWERS){
            saveMeasure("Rabkin", "dichromacy")
            return true
        }
        else if (RABKIN_DEITERANOPY_ANSWERS == RABKIN_MAX_WRONG_ANSWERS){
            saveMeasure("Rabkin", "deiteranopy")
            return true
        }
        else if (RABKIN_PROTANOPY_ANSWERS == RABKIN_MAX_WRONG_ANSWERS){
            saveMeasure("Rabkin", "protanopy")
            return true
        }

        else{
            return false
        }
    }

    override fun doAnswer(sivtsevTest: SivtsevTest, userAnswer: String): Boolean {
        val correctAnswer = sivtsevTest.answer
        if (correctAnswer == userAnswer.uppercase()){

            if (SIVTSEV_NUMBER_TO_SHOW_FROM_LEVEL == 1){
                if (SIVTSEV_CURRENT_LEVEL == SIVTSEV_MAX_LEVEL){
                    SIVTSEV_NUMBER_TO_SHOW_FROM_LEVEL = 2
                    SIVTSEV_CURRENT_LEVEL_WRONG = 0
                    SIVTSEV_CURRENT_LEVEL = 1
                    SIVTSEV_RESULT = "OK"
                    saveMeasure("Sivtsev", "ok")
                    return true
                }
                else {
                    SIVTSEV_NUMBER_TO_SHOW_FROM_LEVEL = 2
                    SIVTSEV_CURRENT_LEVEL_WRONG = 0
                    SIVTSEV_CURRENT_LEVEL += 1
                    return false
                }
            }
            else{
                SIVTSEV_NUMBER_TO_SHOW_FROM_LEVEL -= 1
                return false
            }
        }

        else{
            if (SIVTSEV_CURRENT_LEVEL_WRONG == 1){
                SIVTSEV_NUMBER_TO_SHOW_FROM_LEVEL = 2
                SIVTSEV_CURRENT_LEVEL_WRONG = 0
                SIVTSEV_CURRENT_LEVEL = 1
                SIVTSEV_RESULT = "BAD"
                saveMeasure("Sivtsev", "bad")
                return true
            }
            else{
                SIVTSEV_CURRENT_LEVEL_WRONG += 1
                SIVTSEV_NUMBER_TO_SHOW_FROM_LEVEL += 1

                return false
            }
        }
    }

    override fun saveMeasure(type: String, result: String) {
        val currentDate = SimpleDateFormat("dd.MM.yy HH:mm").format(Date())
        //Remote Saving
        val sharedPrefs = context.getSharedPreferences(PROJECT1729_PREFERENCES, Application.MODE_PRIVATE)
        val userID = sharedPrefs.getString(USERID, "1")
        var newType = ""
        if (type == "Rabkin"){
            newType = "цветоощущение"
        }
        if (type == "Sivtsev"){
            newType = "острота"
        }

        val currentMeasurement = MeasurementRequest(userID = userID?.toInt() ?: 1, type = newType, result = resultConverter(result), date = currentDate)
        saveMeasurement(currentMeasurement)

        //Local saving
        val currentTest = Test(testId = 0, type = type, result = resultConverter(result), dateAndTime = currentDate)
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch{
            database.testDao().insertTest(Converter.convert(test = currentTest))
        }

    }

    private fun resultConverter(result: String): String{
        return when (result){
            "dichromacy"-> "Дихромазия"
            "protanopy" -> "Протанопия"
            "deiteranopy" -> "Дейтеранопия"
            "bad" -> "Близорукость"
            else -> "Норма"

        }
    }

    private fun saveMeasurement(measurementRequest: MeasurementRequest) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            try {
                val response = serverApi.addMeasurement(measurementRequest)

            } catch (e: Throwable) {
                Log.d("Error", e.toString())
            }

        }
    }

    companion object{
        const val USERID = "USERID"
        const val PROJECT1729_PREFERENCES = "PROJECT1729_PREFERENCES"
    }
}