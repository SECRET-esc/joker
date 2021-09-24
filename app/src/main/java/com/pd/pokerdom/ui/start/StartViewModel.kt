package com.pd.pokerdom.ui.start

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pd.pokerdom.model.AppVersion
import com.pd.pokerdom.repository.VersionRepository
import com.pd.pokerdom.ui.BaseViewModel
import kotlinx.coroutines.launch
import java.net.UnknownHostException


class StartViewModel(private val repository: VersionRepository) : BaseViewModel() {


    private val _appVersion = MutableLiveData<AppVersion>()
    val appVersion: LiveData<AppVersion>
        get() = _appVersion

    private val _hostException = MutableLiveData<String>()
    val hostException: LiveData<String>
        get() = _hostException

    fun getAppVersion() {
        uiScope.launch {
            try {
                val result = repository.getAppVersion()
                Log.d("MyTag", "result: $result")
                _appVersion.value = result
            } catch (e: UnknownHostException) {
                _hostException.value = e.message
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}