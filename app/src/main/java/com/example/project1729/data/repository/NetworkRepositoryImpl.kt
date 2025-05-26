package com.example.project1729.data.repository

import android.app.Application
import android.content.Context
import android.util.Log
import com.example.project1729.data.db.Database
import com.example.project1729.data.model.DeleteRequest
import com.example.project1729.data.model.DeleteResponse
import com.example.project1729.data.model.LoginRequest
import com.example.project1729.data.model.RegisterRequest
import com.example.project1729.data.model.User
import com.example.project1729.data.network.ServerApi
import com.example.project1729.domain.converter.Converter
import com.example.project1729.domain.model.UserLogin
import com.example.project1729.domain.model.UserRegister
import com.example.project1729.domain.repository.NetworkRepository
import com.example.project1729.domain.state.TryLoginState
import com.example.project1729.domain.state.TryRegisterState
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.jvm.java


class NetworkRepositoryImpl(val context: Context, val serverApi: ServerApi, val database: Database) : NetworkRepository {

    override fun getRegisteredUsers(user: UserLogin): Flow<TryLoginState> = flow {
        fun getIdFromJson(jsonString: String): Int {
            val gson = Gson()
            val user = gson.fromJson(jsonString, User::class.java)
            return user.id
        }
        emit(TryLoginState.Loading)
        try {
            val request = LoginRequest(login = user.login, password = user.password)
            val response = serverApi.postLogin(request)
            val token = response.body()?.accessToken
            if (token != null) {
                val id = getIdFromJson(response.body()?.user.toString()).toString()
                val sharedPrefs =
                    context.getSharedPreferences(PROJECT1729_PREFERENCES, Application.MODE_PRIVATE)
                sharedPrefs.edit()
                    .putString(USERNAME, user.login)
                    .apply()
                sharedPrefs.edit()
                    .putString(USERID, id)
                    .apply()
                emit(TryLoginState.Success)
                loadRemoteMeasurements(id)
            } else
                emit(TryLoginState.Fail)
        } catch (e: Throwable) {
            emit(TryLoginState.Error(errorMessage = e.toString()))
        }



    }


    override fun registerNewUser(user: UserRegister): Flow<TryRegisterState> = flow {
        val a = DeleteResponse("", "")
        val b = DeleteRequest("", "")
        emit(TryRegisterState.Loading)
        try {
            var diagnoz = ""
            if (user.diagnoz == "") {
                diagnoz = "ok"
            }
            val request =
                RegisterRequest(
                    login = user.login,
                    password = user.password,
                    fio = user.fio,
                    year = user.year,
                    diagnoz = diagnoz,
                )
            Log.d("RRRRRK", request.toString())
            val response = serverApi.post(request)
            val message = response.body()?.message ?: "no message"
            Log.d("RRRRR", message)
            if (message == "Пользователь успешно зарегистрирован") {
                emit(TryRegisterState.Success)
            } else {
                emit(TryRegisterState.Error(errorMessage = "Не удалось зарегистрироваться. Попробуйте другое имя пользователя"))
            }

        } catch (e: Throwable) {
            emit(TryRegisterState.Error(errorMessage = e.toString()))
        }

    }

    private fun loadRemoteMeasurements(userID: String) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch{
            val responseList = serverApi.getMeasurements(userId = userID?.toInt() ?: 1, type = "острота").body()?.measurements
            val testsList = responseList?.map {measurement -> Converter.convert(measurement) } ?: listOf()
            database.testDao().deleteTests("Sivtsev")
            for (test in testsList){
                database.testDao().insertTest(Converter.convert(test = test))
            }

            val responseList2 = serverApi.getMeasurements(userId = userID?.toInt() ?: 1, type = "цветоощущение").body()?.measurements
            val testsList2 = responseList2?.map {measurement -> Converter.convert(measurement) } ?: listOf()
            database.testDao().deleteTests("Rabkin")
            for (test in testsList2){
                database.testDao().insertTest(Converter.convert(test = test))
            }

            val responseList3 = serverApi.getMeasurements(userId = userID?.toInt() ?: 1, type = "кчсм").body()?.measurements
            val testsList3 = responseList3?.map {measurement -> Converter.convert(measurement) } ?: listOf()
            database.testDao().deleteTests("KCHSM")
            for (test in testsList3){
                database.testDao().insertTest(Converter.convert(test = test))
            }



        }
    }

    companion object {
        const val PROJECT1729_PREFERENCES = "PROJECT1729_PREFERENCES"
        const val USERNAME = "USERNAME"
        const val USERID = "USERID"
    }

}