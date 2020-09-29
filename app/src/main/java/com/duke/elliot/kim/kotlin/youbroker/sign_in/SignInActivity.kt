package com.duke.elliot.kim.kotlin.youbroker.sign_in

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.duke.elliot.kim.kotlin.youbroker.R
import com.duke.elliot.kim.kotlin.youbroker.databinding.ActivitySignInBinding
import com.duke.elliot.kim.kotlin.youbroker.sign_in.SignInHelper.Companion.REQUEST_CODE_GOOGLE_SIGN_IN
import com.duke.elliot.kim.kotlin.youbroker.sign_up.IdentityVerificationFragment
import com.duke.elliot.kim.kotlin.youbroker.utility.showToast
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

class SignInActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseExceptionHandler: FirebaseExceptionHandler
    private lateinit var signInHelper: SignInHelper
    private lateinit var viewModel: SignInViewModel
    private val onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
        if (hasFocus) {
            when (view.id) {
                R.id.edit_text_email -> {
                    binding.textInputLayoutEmail.isErrorEnabled = false
                    binding.textInputLayoutEmail.error = null
                }
                R.id.edit_text_password -> {
                    binding.textInputLayoutPassword.isErrorEnabled = false
                    binding.textInputLayoutPassword.error = null
                }
            }
        }
    }

    private val buttonOnClickListener = View.OnClickListener { view ->
        when(view.id) {
            R.id.image_button_facebook_sign_in -> signInHelper.signInWithFacebook()
            R.id.image_button_google_sign_in -> signInHelper.signInWithGoogle()
            R.id.image_button_twitter_sign_in -> signInHelper.signInWithTwitter()
            R.id.button_sign_up -> startIdentityVerificationFragment()
        }
    }

    val callbackManager: CallbackManager? = CallbackManager.Factory.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)

        val viewModelFactory = SignInViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory)[SignInViewModel::class.java]

        binding.editTextEmail.onFocusChangeListener = onFocusChangeListener
        binding.editTextPassword.onFocusChangeListener = onFocusChangeListener

        signInHelper = SignInHelper(this, createFirebaseExceptionHandler())
        initializeButtons()
    }


    private fun initializeButtons() {
        binding.imageButtonFacebookSignIn.setOnClickListener(buttonOnClickListener)
        binding.imageButtonGoogleSignIn.setOnClickListener(buttonOnClickListener)
        binding.imageButtonTwitterSignIn.setOnClickListener(buttonOnClickListener)
        binding.buttonSignUp.setOnClickListener(buttonOnClickListener)
    }

    private fun createFirebaseExceptionHandler() =
        FirebaseExceptionHandler(this).apply {
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
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        viewModel.getTwitterAuthClient().onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_GOOGLE_SIGN_IN -> {
                try {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    val account = task.getResult(ApiException::class.java)
                    signInHelper.firebaseAuthWithGoogle(account)
                } catch (e: ApiException) {
                    showToast(
                        this,
                        "${getString(R.string.failed_to_sign_in_with_google)} \n${e.message}"
                    )
                    e.printStackTrace()
                }
            }
        }
    }

    fun getTwitterAuthClient() = viewModel.getTwitterAuthClient()

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

    fun startIdentityVerificationFragment() {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .setCustomAnimations(
                R.anim.anim_slide_in_from_right,
                R.anim.anim_slide_out_to_right,
                R.anim.anim_slide_in_from_right,
                R.anim.anim_slide_out_to_right
            ).replace(R.id.constraint_layout_activity_sign_in, IdentityVerificationFragment(), TAG).commit()
    }

    companion object {
        private const val TAG = "SignInActivity"
    }
}