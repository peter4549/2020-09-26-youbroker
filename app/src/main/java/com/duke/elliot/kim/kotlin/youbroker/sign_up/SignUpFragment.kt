package com.duke.elliot.kim.kotlin.youbroker.sign_up

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.duke.elliot.kim.kotlin.youbroker.R
import com.duke.elliot.kim.kotlin.youbroker.databinding.FragmentSignUpBinding

class SignUpFragment: Fragment() {

    private lateinit var binding: FragmentSignUpBinding
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
                R.id.edit_text_check_password -> {
                    binding.textInputLayoutCheckPassword.isErrorEnabled = false
                    binding.textInputLayoutCheckPassword.error = null
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)

        binding.editTextEmail.onFocusChangeListener = onFocusChangeListener
        binding.editTextPassword.onFocusChangeListener = onFocusChangeListener
        binding.editTextCheckPassword.onFocusChangeListener = onFocusChangeListener

        return binding.root
    }
}