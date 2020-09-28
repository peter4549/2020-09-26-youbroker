package com.duke.elliot.kim.kotlin.youbroker.sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SignInViewModelFactory (
    private val signInFragment: SignInFragment,
    private val firebaseExceptionHandler: FirebaseExceptionHandler
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
            return SignInViewModel(signInFragment, firebaseExceptionHandler) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}