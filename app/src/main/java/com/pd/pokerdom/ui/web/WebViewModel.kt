package com.pd.pokerdom.ui.web

import com.pd.pokerdom.repository.TokenRepository
import com.pd.pokerdom.storage.SharedPrefsManager
import com.pd.pokerdom.ui.BaseViewModel


class WebViewModel(
    repository: TokenRepository, private val prefs: SharedPrefsManager
) : BaseViewModel() {


}