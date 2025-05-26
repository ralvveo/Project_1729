package com.example.project1729.di

import com.example.project1729.ui.view_model.CheckViewModel
import com.example.project1729.ui.view_model.KchsmViewModel
import com.example.project1729.ui.view_model.DopInfoViewModel
import com.example.project1729.ui.view_model.HistoryContentViewModel
import com.example.project1729.ui.view_model.HistoryViewModel
import com.example.project1729.ui.view_model.LoginViewModel
import com.example.project1729.ui.view_model.RabkinTestViewModel
import com.example.project1729.ui.view_model.RegisterViewModel
import com.example.project1729.ui.view_model.ResultsViewModel
import com.example.project1729.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module{

    viewModel<HistoryViewModel>{
        HistoryViewModel()
    }


    viewModel<SettingsViewModel>{
        SettingsViewModel()
    }

    viewModel<LoginViewModel>{
        LoginViewModel()
    }

    viewModel<RegisterViewModel>{
        RegisterViewModel()
    }

    viewModel<CheckViewModel>{
        CheckViewModel()
    }

    viewModel<DopInfoViewModel>{
        DopInfoViewModel()
    }

    viewModel<ResultsViewModel>{
        ResultsViewModel()
    }

    viewModel<RabkinTestViewModel>{
        RabkinTestViewModel()
    }

    viewModel<HistoryContentViewModel>{
        HistoryContentViewModel()
    }

    viewModel<KchsmViewModel>{
        KchsmViewModel(context = get())
    }
}