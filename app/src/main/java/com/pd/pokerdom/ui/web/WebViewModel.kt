package com.pd.pokerdom.ui.web

import com.pd.pokerdom.model.Token
import com.pd.pokerdom.repository.TokenRepository
import com.pd.pokerdom.storage.SharedPrefsManager
import com.pd.pokerdom.ui.BaseViewModel
import kotlinx.coroutines.launch


class WebViewModel(
    private val repository: TokenRepository,
    private val prefs: SharedPrefsManager
) : BaseViewModel() {


    fun sentTokenToServer(userId: String?, customUserId: String?) {
        ioScope.launch {
            val token = Token(userId = userId, customUserId = customUserId, token = prefs.tokenFCM)
            repository.sendToken(token)
        }
    }
}