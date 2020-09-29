package com.duke.elliot.kim.kotlin.youbroker.sign_in

import android.app.Application
import androidx.lifecycle.ViewModel
import com.duke.elliot.kim.kotlin.youbroker.R
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterConfig
import com.twitter.sdk.android.core.identity.TwitterAuthClient

class SignInViewModel(application: Application) : ViewModel() {
    private val twitterAuthConfig = TwitterAuthConfig(
        application.getString(R.string.twitter_api_key),
        application.getString(R.string.twitter_api_key_secret)
    )

    private val twitterConfig = TwitterConfig.Builder(application)
        .twitterAuthConfig(twitterAuthConfig)
        .build()

    private var twitterAuthClient: TwitterAuthClient

    init {
        Twitter.initialize(twitterConfig)
        twitterAuthClient = TwitterAuthClient()
    }

    fun getTwitterAuthClient() = twitterAuthClient
}