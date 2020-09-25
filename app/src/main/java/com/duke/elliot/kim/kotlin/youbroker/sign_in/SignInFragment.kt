package com.duke.elliot.kim.kotlin.youbroker.sign_in

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.duke.elliot.kim.kotlin.youbroker.R
import com.duke.elliot.kim.kotlin.youbroker.Utilities.showToast
import com.duke.elliot.kim.kotlin.youbroker.databinding.FragmentSignInBinding
import com.duke.elliot.kim.kotlin.youbroker.sign_in.SignInHelper.REQUEST_CODE_GOOGLE_SIGN_IN
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

class SignInFragment: Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private lateinit var firebaseExceptionHandler: FirebaseExceptionHandler
    private lateinit var viewModel: SignInViewModel
    private val socialSignInButtonsOnClickListener = View.OnClickListener {
        when(it.id) {
            R.id.image_button_apple_sign_in -> {  }
            R.id.image_button_facebook_sign_in -> {  }
            R.id.image_button_google_sign_in -> SignInHelper.signInWithGoogle(this)
            R.id.image_button_twitter_sign_in -> {  }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false)

        firebaseExceptionHandler = FirebaseExceptionHandler(requireContext())

        initializeSocialSignInButtons()

        return binding.root
    }

    private fun initializeSocialSignInButtons() {
        binding.imageButtonGoogleSignIn.setOnClickListener(socialSignInButtonsOnClickListener)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_GOOGLE_SIGN_IN -> {
                    try {
                        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                        val account = task.getResult(ApiException::class.java)
                        SignInHelper.firebaseAuthWithGoogle(this, account)
                    } catch (e: ApiException) {
                        showToast(
                            requireContext(),
                            getString(R.string.failed_to_sign_in_with_google)
                        )
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    fun getFirebaseExceptionHandler(): FirebaseExceptionHandler? {
        return if (::firebaseExceptionHandler.isInitialized)
            firebaseExceptionHandler
        else
            null
    }
}