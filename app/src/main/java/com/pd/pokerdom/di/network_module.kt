package com.pd.pokerdom.di

import com.pd.pokerdom.BuildConfig
import com.pd.pokerdom.api.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {

    single {
        HttpLoggingInterceptor()
            .setLevel(
                if (BuildConfig.DEBUG) Level.BODY else Level.NONE
            )
    }

    single {
        OkHttpClient.Builder()
                .addInterceptor(get<HttpLoggingInterceptor>())
                .build()
    }

    single {
        Retrofit.Builder()
            .client(get())
            .baseUrl("https://s.dev1.btcasino.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    factory{ get<Retrofit>().create(ApiService::class.java) }
}