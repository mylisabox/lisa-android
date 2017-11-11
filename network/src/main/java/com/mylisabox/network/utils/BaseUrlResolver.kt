package com.mylisabox.network.utils

import com.mylisabox.network.preferences.Preferences
import com.mylisabox.network.preferences.PreferencesProvider
import io.reactivex.Single
import javax.inject.Inject

class BaseUrlResolver @Inject constructor(private val mdnsFinder: MdnsFinder, private val preferencesProvider: PreferencesProvider) {
    fun getBaseUrl(): Single<String> {
        return mdnsFinder.searchLISAService()
                .flatMap({ hostPort -> Single.just(hostPort) })
                .onErrorResumeNext({ thowable ->
                    val externalUrl: String? = preferencesProvider.getPreferences().get(Preferences.KEY_EXTERNAL_URL)
                    if (externalUrl == null) {
                        return@onErrorResumeNext mdnsFinder.searchLISAService()
                                .flatMap({ hostPort ->
                                    Single.just(hostPort)
                                            .onErrorResumeNext { Single.error(thowable) }
                                })
                    } else {
                        return@onErrorResumeNext Single.just(externalUrl)
                    }
                })
    }
}