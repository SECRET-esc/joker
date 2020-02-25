package com.pd.pokerdom.ui.web

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pd.pokerdom.model.TokenObj
import com.pd.pokerdom.repository.TokenRepository
import com.pd.pokerdom.storage.SharedPrefsManager
import com.pd.pokerdom.ui.BaseViewModel
import kotlinx.coroutines.launch


class WebViewModel(
    private val repository: TokenRepository,
    private val prefs: SharedPrefsManager
) : BaseViewModel() {

    private val _configDomain = MutableLiveData<String>(prefs.configDomain)
    val configDomain: LiveData<String>
        get() = _configDomain

    fun sentTokenToServer(userId: String?, customUserId: String?) {
        prefs.userId = userId
        prefs.customUserId = customUserId

        uiScope.launch {
            val token = TokenObj(userId = userId, customUserId = customUserId, token = prefs.tokenFCM)
            Log.d("MyTag", "$token")
            try {
                repository.sendToken(token)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}