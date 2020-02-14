package com.pd.pokerdom.di

import com.pd.pokerdom.repository.TokenRepository
import com.pd.pokerdom.repository.VersionRepository
import org.koin.dsl.module

val repositoryModule = module {
    factory { VersionRepository(get()) }
    factory { TokenRepository(get()) }
}