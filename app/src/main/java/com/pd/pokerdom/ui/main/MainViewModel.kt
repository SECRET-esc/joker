package com.pd.pokerdom.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pd.pokerdom.storage.SharedPrefsManager
import com.pd.pokerdom.ui.BaseViewModel


class MainViewModel(private val prefs: SharedPrefsManager) : BaseViewModel() {

    private val _configDomain = MutableLiveData<String>(prefs.configDomain)
    val configDomain: LiveData<String>
        get() = _configDomain

}