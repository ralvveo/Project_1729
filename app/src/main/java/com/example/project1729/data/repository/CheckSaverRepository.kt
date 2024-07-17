package com.example.project1729.data.repository

import com.example.project1729.data.db.Database
import com.example.project1729.domain.converter.Converter
import com.example.project1729.domain.model.Check

class CheckSaverRepository (private val database: Database){

    suspend fun saveCheck(check: Check){
        database.checkDao().insertCheck(Converter.convert(check))
    }

    suspend fun getChecksList(): List<Check>{
        return database.checkDao().getChecksList().map{checkEntity -> Converter.convert(checkEntity)}
    }
}