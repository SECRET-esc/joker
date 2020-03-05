package com.pd.pokerdom.di

import com.pd.pokerdom.ui.main.MainViewModel
import com.pd.pokerdom.ui.srart.StartViewModel
import com.pd.pokerdom.ui.web.WebViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { StartViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { WebViewModel(get(), get()) }
}
