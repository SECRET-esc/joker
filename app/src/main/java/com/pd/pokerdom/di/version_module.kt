package com.pd.pokerdom.di

import com.pd.pokerdom.ui.version.VersionControl
import org.koin.dsl.module

val versionModule = module {
    single {VersionControl(get()) }
}