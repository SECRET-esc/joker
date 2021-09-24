package com.pd.pokerdom.ui.version

import android.util.Log
import com.pd.pokerdom.BuildConfig
import com.pd.pokerdom.repository.VersionRepository
import com.pd.pokerdom.ui.BaseViewModel
import io.github.g00fy2.versioncompare.Version
import kotlinx.coroutines.runBlocking
import java.net.UnknownHostException

class VersionControl(private val repository: VersionRepository): BaseViewModel() {

    fun getVersion(): Array<Any> {
        return runBlocking {
             getAppVersion()
        }
    }

    private suspend fun getAppVersion(): Array<Any> {
        try {
            val result = repository.getAppVersion()
            Log.d("MyTag", "result: $result")
            return compareVersion(result.versionLimit.toString(), result.site.toString())
        } catch (e: UnknownHostException) {
            print(e.message)
            return arrayOf()
        } catch (e: Exception) {
            print(e.message)
            return arrayOf()
        }
    }

    private fun compareVersion(versionLimit: String, originSite: String): Array<Any> {
         if (Version(versionLimit).isHigherThan(BuildConfig.VERSION_NAME)) {
            val bool = Version(versionLimit).isHigherThan(BuildConfig.VERSION_NAME)
            Log.d("MyLog", "[VERSION_NAME] --- $bool")
             return arrayOf(true, originSite)
        } else {
            val bool = Version(versionLimit).isHigherThan(BuildConfig.VERSION_NAME)
            Log.d("MyLog", "[VERSION_NAME] $bool $versionLimit")
            print(arrayOf(false, originSite))
            return arrayOf(false, originSite)
        }
    }
}