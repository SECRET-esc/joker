package com.pd.pokerdom.di

import com.pd.pokerdom.repository.TokenRepository
import org.koin.dsl.module

val repositoryModule = module {
    factory { TokenRepository(get()) }
}