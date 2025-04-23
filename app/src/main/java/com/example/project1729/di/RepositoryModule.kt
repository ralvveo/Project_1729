package com.example.project1729.di

import com.example.project1729.data.repository.CheckSaverRepository
import com.example.project1729.data.repository.HistoryContentUpdaterRepositoryImpl
import com.example.project1729.data.repository.HistoryUpdaterRepositoryImpl
import com.example.project1729.data.repository.RabkinRepositoryImpl
import com.example.project1729.data.repository.ThemeSwitcherRepositoryImpl
import com.example.project1729.data.repository.RetrofitSheetsRepositoryImpl
import com.example.project1729.domain.repository.HistoryContentUpdaterRepository
import com.example.project1729.domain.repository.HistoryUpdaterRepository
import com.example.project1729.domain.repository.RabkinRepository
import com.example.project1729.domain.repository.ThemeSwitcherRepository
import com.example.project1729.domain.repository.RetrofitSheetsRepository
import org.koin.dsl.module

val repositoryModule = module {

    factory<HistoryUpdaterRepository>{
        HistoryUpdaterRepositoryImpl(context = get())
    }

    factory<ThemeSwitcherRepository>{
        ThemeSwitcherRepositoryImpl(context = get())
    }

    factory<RetrofitSheetsRepository>{
        RetrofitSheetsRepositoryImpl(context = get(), sheetsSearchApi = get())
    }

    factory<CheckSaverRepository>{
        CheckSaverRepository(database = get())
    }


    factory<RabkinRepository>{
        RabkinRepositoryImpl(database = get())
    }

    factory<HistoryContentUpdaterRepository>{
        HistoryContentUpdaterRepositoryImpl(database = get())
    }
}