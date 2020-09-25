package com.duke.elliot.kim.kotlin.youbroker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.duke.elliot.kim.kotlin.youbroker.Utilities.printHashKey
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupTimber()

        printHashKey(this)
    }

    private fun setupTimber() {
        Timber.plant(Timber.DebugTree())
    }
}