package com.duke.elliot.kim.kotlin.youbroker.sign_in

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.duke.elliot.kim.kotlin.youbroker.MainViewModel
import com.duke.elliot.kim.kotlin.youbroker.MainViewModelFactory
import com.duke.elliot.kim.kotlin.youbroker.R
import com.duke.elliot.kim.kotlin.youbroker.databinding.FragmentSignInBinding
import com.duke.elliot.kim.kotlin.youbroker.sign_in.SignInHelper.Companion.REQUEST_CODE_GOOGLE_SIGN_IN
import com.duke.elliot.kim.kotlin.youbroker.utility.showToast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

class SignInFragment: Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private lateinit var firebaseExceptionHandler: FirebaseExceptionHandler
    // private lateinit var signInHelper: SignInHelper
    private lateinit var viewModel: SignInViewModel

    /*
    private val socialSignInButtonsOnClickListener = View.OnClickListener {
        when(it.id) {
            R.id.image_button_facebook_sign_in -> signInHelper.signInWithFacebook()
            R.id.image_button_google_sign_in -> signInHelper.signInWithGoogle()
            R.id.image_button_twitter_sign_in -> signInHelper.signInWithTwitter()
        }
    }

     */

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false)

        firebaseExceptionHandler = createFirebaseExceptionHandler()
        val viewModelFactory = SignInViewModelFactory(this, firebaseExceptionHandler)
        viewModel = ViewModelProvider(this, viewModelFactory)[SignInViewModel::class.java]

        binding.signInViewModel = viewModel

        // signInHelper = SignInHelper(this, firebaseExceptionHandler)
        binding.buttonSignIn.setOnClickListener {
            signInWithEmail()
        }
        // initializeSocialSignInButtons()

        return binding.root
    }

    /*
    private fun initializeSocialSignInButtons() {
        binding.imageButtonFacebookSignIn.setOnClickListener(socialSignInButtonsOnClickListener)
        binding.imageButtonGoogleSignIn.setOnClickListener(socialSignInButtonsOnClickListener)
        binding.imageButtonTwitterSignIn.setOnClickListener(socialSignInButtonsOnClickListener)
    }

     */

    private fun createFirebaseExceptionHandler() =
        FirebaseExceptionHandler(requireContext()).apply {
            setExceptionFunction(FirebaseExceptionHandler.ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL) {
                binding.textInputLayoutEmail.isErrorEnabled = true
                binding.textInputLayoutEmail.error = this
                    .getErrorText(FirebaseExceptionHandler.ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL)
                binding.editTextEmail.requestFocus()
            }

            setExceptionFunction(FirebaseExceptionHandler.ERROR_EMAIL_ALREADY_IN_USE) {
                binding.textInputLayoutEmail.isErrorEnabled = true
                binding.textInputLayoutEmail.error = this
                    .getErrorText(FirebaseExceptionHandler.ERROR_EMAIL_ALREADY_IN_USE)
                binding.editTextEmail.requestFocus()
            }

            setExceptionFunction(FirebaseExceptionHandler.ERROR_INVALID_EMAIL) {
                binding.textInputLayoutEmail.isErrorEnabled = true
                binding.textInputLayoutEmail.error = this
                    .getErrorText(FirebaseExceptionHandler.ERROR_INVALID_EMAIL)
                binding.editTextEmail.requestFocus()
            }

            setExceptionFunction(FirebaseExceptionHandler.ERROR_USER_NOT_FOUND) {
                binding.textInputLayoutEmail.isErrorEnabled = true
                binding.textInputLayoutEmail.error = this
                    .getErrorText(FirebaseExceptionHandler.ERROR_USER_NOT_FOUND)
                binding.editTextEmail.requestFocus()
            }

            setExceptionFunction(FirebaseExceptionHandler.ERROR_WEAK_PASSWORD) {
                binding.textInputLayoutPassword.isErrorEnabled = true
                binding.textInputLayoutPassword.error = this
                    .getErrorText(FirebaseExceptionHandler.ERROR_WEAK_PASSWORD)
            }

            setExceptionFunction(FirebaseExceptionHandler.ERROR_WRONG_PASSWORD) {
                binding.textInputLayoutPassword.isErrorEnabled = true
                binding.textInputLayoutPassword.error = this
                    .getErrorText(FirebaseExceptionHandler.ERROR_WRONG_PASSWORD)
            }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_GOOGLE_SIGN_IN -> {
                try {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    val account = task.getResult(ApiException::class.java)
                    viewModel.getSignInHelper().firebaseAuthWithGoogle(account)
                } catch (e: ApiException) {
                    showToast(
                        requireContext(),
                        "${getString(R.string.failed_to_sign_in_with_google)} \n${e.message}"
                    )
                    e.printStackTrace()
                }
            }
        }
    }

    private fun signInWithEmail() {
        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()

        if (email.isBlank()) {
            binding.textInputLayoutEmail.isErrorEnabled = true
            binding.textInputLayoutEmail.error = getString(R.string.please_enter_your_email)
            binding.editTextEmail.requestFocus()
            return
        }

        if (password.isBlank()) {
            binding.textInputLayoutPassword.isErrorEnabled = true
            binding.textInputLayoutPassword.error = getString(R.string.please_enter_your_password)
            binding.editTextPassword.requestFocus()
            return
        }

        // signInHelper.signInWithEmail(email, password)
    }
}