package com.duke.elliot.kim.kotlin.youbroker.sign_in

import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import com.duke.elliot.kim.kotlin.youbroker.utility.getProgressDialog
import com.duke.elliot.kim.kotlin.youbroker.utility.showToast

class SignInViewModel(
    private val signInFragment: SignInFragment,
    firebaseExceptionHandler: FirebaseExceptionHandler
) : ViewModel() {
    private val signInHelper = SignInHelper(signInFragment, firebaseExceptionHandler)

    fun getSignInHelper() = signInHelper

    fun signInWithFacebook() {
        signInHelper.signInWithFacebook()
    }

    fun signInWithGoogle() {
        signInHelper.signInWithGoogle()
    }

    fun signInWithTwitter() {
        signInHelper.signInWithTwitter()
    }
}