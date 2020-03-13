package com.pd.pokerdom.ui.srart

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pd.pokerdom.model.AppVersion
import com.pd.pokerdom.repository.VersionRepository
import com.pd.pokerdom.ui.BaseViewModel
import com.pd.pokerdom.util.checkForUpdate
import kotlinx.coroutines.launch
import java.net.UnknownHostException


class StartViewModel(private val repository: VersionRepository) : BaseViewModel() {

    private val _appVersion = MutableLiveData<AppVersion>()
    val appVersion: LiveData<AppVersion>
        get() = _appVersion

    private val _hostException = MutableLiveData<String>()
    val hostException: LiveData<String>
        get() = _hostException

    private val _openMain = MutableLiveData<Boolean>()
    val openMain: LiveData<Boolean>
        get() = _openMain

    fun getAppVersion() {
        uiScope.launch {
            try {
                val result = repository.getAppVersion()
                Log.d("MyTag", "result: $result")
                _appVersion.value = result
            } catch (e: UnknownHostException) {
                _hostException.value = e.message
                _openMain.value = true
            } catch (e: Exception) {
                e.printStackTrace()
                _openMain.value = true
            }
        }
    }

}