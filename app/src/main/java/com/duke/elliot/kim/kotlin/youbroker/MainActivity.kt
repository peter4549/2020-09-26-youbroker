package com.duke.elliot.kim.kotlin.youbroker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
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

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupTimber()

        val viewModelFactory = MainViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        printHashKey(this)
    }

    private fun setupTimber() {
        Timber.plant(Timber.DebugTree())
    }
}