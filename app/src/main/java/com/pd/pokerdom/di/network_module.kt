package com.pd.pokerdom.di


import com.pd.pokerdom.BuildConfig
import com.pd.pokerdom.api.ApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor.*
import org.koin.dsl.module

val networkModule = module {

    factory<Interceptor> {
        HttpLoggingInterceptor()
            .setLevel(
                if (BuildConfig.DEBUG) Level.BODY else Level.NONE
            )
    }

    factory {
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