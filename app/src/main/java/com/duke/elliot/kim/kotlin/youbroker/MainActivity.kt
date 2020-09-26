package com.duke.elliot.kim.kotlin.youbroker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.duke.elliot.kim.kotlin.youbroker.utility.printHashKey
import com.duke.elliot.kim.kotlin.youbroker.utility.showToast
import com.facebook.CallbackManager
import com.google.firebase.auth.FirebaseAuth
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterConfig
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var twitterAuthClient: TwitterAuthClient
    val callbackManager: CallbackManager? = CallbackManager.Factory.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupTimber()

        // TODO: Move to ViewModel
        // From
        val twitterAuthConfig = TwitterAuthConfig(
            getString(R.string.twitter_api_key),
            getString(R.string.twitter_api_key_secret)
        )
        val twitterConfig = TwitterConfig.Builder(this)
            .twitterAuthConfig(twitterAuthConfig)
            .build()
        Twitter.initialize(twitterConfig)
        twitterAuthClient = TwitterAuthClient()

        setAuthStateListener()
        // To

        printHashKey(this)
    }

    private fun setupTimber() {
        Timber.plant(Timber.DebugTree())
    }

    fun getTwitterAuthClient() = twitterAuthClient

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        twitterAuthClient.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
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
        showToast(this, getString(R.string.signed_out))
    }
}