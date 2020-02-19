package com.pd.pokerdom.ui.web

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pd.pokerdom.model.Token
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
        ioScope.launch {
            val token = Token(userId = userId, customUserId = customUserId, token = prefs.tokenFCM)
            repository.sendToken(token)
        }
    }
}