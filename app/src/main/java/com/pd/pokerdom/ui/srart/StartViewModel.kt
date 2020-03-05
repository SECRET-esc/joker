package com.pd.pokerdom.ui.srart

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pd.pokerdom.model.AppVersion
import com.pd.pokerdom.repository.VersionRepository
import com.pd.pokerdom.ui.BaseViewModel
import kotlinx.coroutines.launch


class StartViewModel(private val repository: VersionRepository) : BaseViewModel() {


    private val _appVersion = MutableLiveData<AppVersion>()
    val appVersion: LiveData<AppVersion>
        get() = _appVersion

    fun getAppVersion() {
        uiScope.launch {
            try {
                val result = repository.getAppVersion()
                Log.d("MyTag", "result: $result")
                _appVersion.value = result
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}