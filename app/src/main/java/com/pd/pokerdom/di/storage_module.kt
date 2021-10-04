package com.pd.pokerdom.di

import com.pd.pokerdom.storage.SharedPrefsManager
import com.pd.pokerdom.ui.ApplicationState
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val storageModule = module {
    single { SharedPrefsManager(androidContext()) }
    single { ApplicationState() }
}