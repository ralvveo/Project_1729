package com.example.project1729.data.repository

import android.content.Context
import android.view.View
import com.example.project1729.data.network.SheetsSearchApi
import com.example.project1729.domain.converter.UserConverter
import com.example.project1729.domain.model.UserLogin
import com.example.project1729.domain.model.UserRegister
import com.example.project1729.domain.repository.RetrofitSheetsRepository
import com.example.project1729.domain.state.TryLoginState
import com.example.project1729.domain.state.TryRegisterState
import com.example.project1729.utils.md5
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json


class RetrofitSheetsRepositoryImpl(val context: Context, val sheetsSearchApi: SheetsSearchApi) : RetrofitSheetsRepository {

    override fun getRegisteredUsers(user: UserLogin): Flow<TryLoginState> = flow{

            val userConverter = UserConverter()
            emit(TryLoginState.Loading)
            val userList = mutableListOf<UserRegister>()
            try{

                val response = sheetsSearchApi.get()
                val data = response.getAsJsonArray("users").toList()
                for (userJson in data) {
                    val userJsonString = userJson.toString()
                    val currentUser = Json.decodeFromString<UserRegister>(userJsonString)
                    userList.add(currentUser)
                }

                val userLoginList = userList.map{userRegister ->  userConverter.convert(userRegister)}
                val userMd5 = user.copy(password = user.password.md5())
                if (userMd5 in userLoginList)
                    emit(TryLoginState.Success)
                else
                    emit(TryLoginState.Fail)
            }
            catch(e:Throwable){
                emit(TryLoginState.Error(errorMessage = e.toString()))
            }
    }


    override fun registerNewUser(user: UserRegister): Flow<TryRegisterState> = flow {
        emit(TryRegisterState.Loading)

        val userList = mutableListOf<UserRegister>()
        try{

            val response = sheetsSearchApi.get()
            val data = response.getAsJsonArray("users").toList()
            for (userJson in data) {
                val userJsonString = userJson.toString()
                val currentUser = Json.decodeFromString<UserRegister>(userJsonString)
                userList.add(currentUser)
            }

            val userLoginNamesList = userList.map{userRegister ->  userRegister.login}

            if (user.login in userLoginNamesList)
                emit(TryRegisterState.Error(errorMessage = "User with this login already exists! Try another login!"))
            else{

                try {
                    val response = sheetsSearchApi.post(
                        user.fio,
                        user.year,
                        user.diagnoz,
                        user.login,
                        user.password.md5()

                    )
                    emit(TryRegisterState.Success)

                } catch (e: Throwable) {
                    emit(TryRegisterState.Error(errorMessage = e.toString()))
                }

            }

        }
        catch(e:Throwable){
            emit(TryRegisterState.Error(errorMessage = e.toString()))
        }

    }


}