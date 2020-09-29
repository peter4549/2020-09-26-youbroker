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

    init {
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
}

