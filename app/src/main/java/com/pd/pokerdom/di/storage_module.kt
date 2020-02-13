package com.pd.pokerdom.di

import com.pd.pokerdom.storage.SharedPrefsManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val storageModule = module {
    single { SharedPrefsManager(androidContext()) }
}