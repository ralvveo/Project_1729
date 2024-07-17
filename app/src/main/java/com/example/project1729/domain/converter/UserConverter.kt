package com.example.project1729.domain.converter

import com.example.project1729.domain.model.UserLogin
import com.example.project1729.domain.model.UserRegister

class UserConverter {

    fun convert(userRegister: UserRegister): UserLogin{
        return UserLogin(login = userRegister.login, password = userRegister.password)
    }


}