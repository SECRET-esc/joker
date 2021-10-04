package com.pd.pokerdom.ui

import android.util.Log
import io.reactivex.rxjava3.subjects.ReplaySubject

class ApplicationState(){
    private var isFirstLunch: Boolean = false;
    private val isFromNotification: ReplaySubject<Boolean> = ReplaySubject.create();

    private val TAG: String = "SubscriberSubject";

    fun getApplicationState(): ReplaySubject<Boolean> {
        return isFromNotification
    }

    fun fromNotification() {
        Log.d(TAG, "fromNotification")
        isFromNotification.onNext(true);
    }
    fun fromActivity() {
        Log.d(TAG, "fromActivity")
        isFromNotification.onNext(false);
    }

    fun initActivity(): ReplaySubject<Boolean> {
        Log.d(TAG, "initActivity $isFirstLunch")
        fromActivity()
        return isFromNotification
    }

}