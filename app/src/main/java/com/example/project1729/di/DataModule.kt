package com.example.project1729.di

import androidx.room.Room
import com.example.project1729.App.Companion.MAIN_URL2
import com.example.project1729.data.db.Database
import com.example.project1729.data.network.SheetsSearchApi
import com.google.gson.GsonBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module{


    single<SheetsSearchApi> {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setLenient()
        val gson = gsonBuilder.create()
        Retrofit.Builder()
            .baseUrl(MAIN_URL2)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(SheetsSearchApi::class.java)
    }

    single {
        Room.databaseBuilder(androidContext(), Database::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }


}