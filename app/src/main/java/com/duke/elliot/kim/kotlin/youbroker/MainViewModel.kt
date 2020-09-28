package com.duke.elliot.kim.kotlin.youbroker

import android.app.Application
import androidx.lifecycle.ViewModel
import com.duke.elliot.kim.kotlin.youbroker.utility.showToast
import com.google.firebase.auth.FirebaseAuth
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterConfig
import com.twitter.sdk.android.core.identity.TwitterAuthClient

class MainViewModel(private val application: Application) : ViewModel() {

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
        setAuthStateListener()
    }

    private fun setAuthStateListener() {
        FirebaseAuth.getInstance().addAuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser != null)
                eventAfterSignIn()
            else
                eventAfterSignOut()
        }
    }

    private val eventAfterSignIn = {
        // showToast(this, getString(R.string.signed_in))
        // TODO: Load user information
    }

    private val eventAfterSignOut = {
        showToast(application, application.getString(R.string.signed_out))
    }

    fun getTwitterAuthClient() = twitterAuthClient
}

