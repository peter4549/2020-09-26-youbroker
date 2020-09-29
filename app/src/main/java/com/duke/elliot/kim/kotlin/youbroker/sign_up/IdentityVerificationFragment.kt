package com.duke.elliot.kim.kotlin.youbroker.sign_up

import android.content.Context
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.duke.elliot.kim.kotlin.youbroker.R
import com.duke.elliot.kim.kotlin.youbroker.databinding.FragmentIdentityVerificationBinding
import com.duke.elliot.kim.kotlin.youbroker.sign_in.FirebaseExceptionHandler
import com.duke.elliot.kim.kotlin.youbroker.utility.showToast
import com.google.firebase.FirebaseApiNotAvailableException
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timer

class IdentityVerificationFragment: Fragment() {

    private lateinit var binding: FragmentIdentityVerificationBinding
    private var timeout = TIME_OUT
    private var timer: Timer? = null
    private var resendingToken: PhoneAuthProvider.ForceResendingToken? = null
    private var verificationCode: String? = null
    private var verified = false

    private val callbacks = object :
        PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
            if (phoneAuthCredential.smsCode != null)
                verificationCode = phoneAuthCredential.smsCode
            else
                showToast(requireContext(), getString(R.string.code_lost))
        }
        override fun onVerificationFailed(e: FirebaseException) {
            handleException(e)
        }

        override fun onCodeSent(
            verificationId: String,
            forceResendingToken: PhoneAuthProvider.ForceResendingToken
        ) {
            super.onCodeSent(verificationId, forceResendingToken)
            showToast(requireContext(), getString(R.string.code_sent))
            //uiController.updateUi(STATE_VERIFICATION_CODE_SENT)
            //resendingToken = forceResendingToken
            //text_input_layout_verification_code.error = null
            //text_input_layout_verification_code.isErrorEnabled = false
            countTime()
        }
    }

    private val onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
        if (hasFocus) {
            when (view.id) {
                R.id.edit_text_phone_number -> {
                    binding.textInputLayoutPhoneNumber.isErrorEnabled = false
                    binding.textInputLayoutPhoneNumber.error = null
                }
                R.id.edit_text_verification_code -> {
                    binding.textInputLayoutVerificationCode.isErrorEnabled = false
                    binding.textInputLayoutVerificationCode.error = null
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_identity_verification, container, false)

        binding.editTextPhoneNumber.onFocusChangeListener = onFocusChangeListener
        binding.editTextVerificationCode.onFocusChangeListener = onFocusChangeListener

        return binding.root
    }

    private fun sendCode(phoneNumber: String) {
        val job = Job()
        CoroutineScope(Dispatchers.IO + job).launch {
            val countryCode = (requireContext().getSystemService(Context.TELEPHONY_SERVICE)
                    as TelephonyManager).networkCountryIso.toUpperCase(Locale.ROOT)
            try {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    PhoneNumberUtils.formatNumberToE164(
                        phoneNumber,
                        countryCode
                    ),
                    TIME_OUT,
                    TimeUnit.SECONDS,
                    requireActivity(),
                    callbacks,
                    resendingToken
                )
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                launch(Dispatchers.Main + job) {
                    binding.textInputLayoutPhoneNumber.isErrorEnabled = true
                    binding.textInputLayoutPhoneNumber.error = getString(R.string.invalid_phone_number)
                }
            }
        }
    }

    /*
    private fun verifyCode(code: String) {
        if (verificationCode != null) {
            if (code == verificationCode) {
                showToast(requireContext(), getString(R.string.verified))
                verified = true
                stopTimer()
                // uiController.updateUi(STATE_VERIFIED)
            } else {
                text_input_layout_verification_code.isErrorEnabled = true
                text_input_layout_verification_code.error = getString(R.string.verification_code_mismatch)
            }
        }
    }

    private fun signUp() {
        val email = edit_text_email.text.toString()
        val password = edit_text_password.text.toString()

        if (email.isBlank()) {
            showToast(requireContext(), getString(R.string.please_enter_your_email))
            return
        }

        if (password.isBlank()) {
            showToast(requireContext(), getString(R.string.please_enter_your_password))
            return
        }

        (requireActivity() as MainActivity).firebaseAuth
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.d("account created")
                    hideKeyboard(requireContext(), fragmentView)
                }
                else
                    firebaseExceptionHandler.exceptionHandling(task.exception as FirebaseException)
            }
    }

     */

    private fun countTime() {
        timer = timer(period = 1000) {
            if (timeout >= 0) {
                val minute = timeout / 60
                val seconds = timeout % 60

                --timeout

                var textSeconds = seconds.toString()

                if (seconds < 10)
                    textSeconds = "0$textSeconds"

                val text = "$minute:$textSeconds"

                requireActivity().runOnUiThread {
                    binding.textTimer.text = text
                }
            } else {
                timeout = TIME_OUT
                requireActivity().runOnUiThread {
                    stopTimer()
                    /*
                    text_input_layout_verification_code.isErrorEnabled = true
                    text_input_layout_verification_code.error = getString(R.string.timed_out)
                    button_send_verification_code.text = getString(R.string.send_verification_code)
                    resendingToken = null

                     */
                }
            }
        }
    }

    private fun stopTimer() {
        timer?.cancel()
        binding.textTimer.text = ""
    }

    private fun handleException(e: Exception) {
        when (e) {
            is FirebaseAuthInvalidCredentialsException -> {  }
            is FirebaseAuthException -> {  }
            is FirebaseTooManyRequestsException -> {  }
            is FirebaseApiNotAvailableException -> {
                showToast(requireContext(), getString(R.string.firebase_api_not_available_exception_identity_verification))
            }
        }
    }

    companion object {
        private const val STATE_VERIFICATION_CODE_SENT = 1
        private const val STATE_VERIFIED = 2
        private const val TIME_OUT = 120L
    }
}