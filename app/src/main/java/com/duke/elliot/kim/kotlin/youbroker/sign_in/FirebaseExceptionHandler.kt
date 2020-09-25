package com.duke.elliot.kim.kotlin.youbroker.sign_in

import android.content.Context
import com.duke.elliot.kim.kotlin.youbroker.R
import com.duke.elliot.kim.kotlin.youbroker.Utilities.showToast
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthException
import timber.log.Timber

class FirebaseExceptionHandler(private val context: Context) {

    private val authErrorCodeFunctionMap = mutableMapOf<String, () -> Unit>()
    private val authErrorCodeTextMap: Map<String, String> = mapOf(
        ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL to context.getString(R.string.account_exists_with_different_credential),
        ERROR_CREDENTIAL_ALREADY_IN_USE to context.getString(R.string.credential_already_in_use),
        ERROR_CUSTOM_TOKEN_MISMATCH to context.getString(R.string.custom_token_mismatch),
        ERROR_EMAIL_ALREADY_IN_USE to context.getString(R.string.email_already_in_use),
        ERROR_INVALID_CREDENTIAL to context.getString(R.string.invalid_credential),
        ERROR_INVALID_CUSTOM_TOKEN to context.getString(R.string.invalid_custom_token),
        ERROR_INVALID_EMAIL to context.getString(R.string.invalid_email),
        ERROR_INVALID_USER_TOKEN to context.getString(R.string.invalid_user_token),
        ERROR_OPERATION_NOT_ALLOWED to context.getString(R.string.operation_not_allowed),
        ERROR_REQUIRES_RECENT_LOGIN to context.getString(R.string.requires_recent_login),
        ERROR_USER_DISABLED to context.getString(R.string.user_disabled),
        ERROR_USER_MISMATCH to context.getString(R.string.user_mismatch),
        ERROR_USER_NOT_FOUND to context.getString(R.string.user_not_found),
        ERROR_USER_TOKEN_EXPIRED to context.getString(R.string.user_token_expired),
        ERROR_WEAK_PASSWORD to context.getString(R.string.weak_password),
        ERROR_WRONG_PASSWORD to context.getString(R.string.wrong_password)
    )
    private val unknownErrorMessage = context.getString(R.string.unknown_error)

    fun handleException(e: FirebaseException, `throw`: Boolean = false) {
        when (e) {
            is FirebaseAuthException -> handleException(e, `throw`)
            is FirebaseTooManyRequestsException -> {
                Timber.e("${context.getString(R.string.app_name)} FirebaseTooManyRequestsException: ${e.message}")
                e.printStackTrace()
                showToast(context, context.getString(R.string.too_many_requests))
            }
            is FirebaseNetworkException -> {
                Timber.e("${context.getString(R.string.app_name)} FirebaseNetWorkException: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    private fun handleAuthException(e: FirebaseAuthException, `throw`: Boolean = false) {
        if (`throw`)
            throw e

        authErrorCodeTextMap[e.errorCode]?.let { showToast(context, it) }

        Timber.e("${context.getString(R.string.app_name)} FirebaseAuthException: ${e.message}")
        e.printStackTrace()

        invokeExceptionFunction(e.errorCode)
    }

    fun setExceptionFunction(key: String, errorFunction: () -> Unit) {
        authErrorCodeFunctionMap[key] = errorFunction
    }

    private fun invokeExceptionFunction(key: String) {
        authErrorCodeFunctionMap[key]?.invoke()
    }

    companion object {
        const val ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL = "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL"
        const val ERROR_CREDENTIAL_ALREADY_IN_USE = "ERROR_CREDENTIAL_ALREADY_IN_USE"
        const val ERROR_CUSTOM_TOKEN_MISMATCH = "ERROR_CUSTOM_TOKEN_MISMATCH"
        const val ERROR_EMAIL_ALREADY_IN_USE = "ERROR_EMAIL_ALREADY_IN_USE"
        const val ERROR_INVALID_CREDENTIAL = "ERROR_INVALID_CREDENTIAL"
        const val ERROR_INVALID_CUSTOM_TOKEN = "ERROR_INVALID_CUSTOM_TOKEN"
        const val ERROR_INVALID_EMAIL = "ERROR_INVALID_EMAIL"
        const val ERROR_INVALID_USER_TOKEN = "ERROR_INVALID_USER_TOKEN"
        const val ERROR_OPERATION_NOT_ALLOWED = "ERROR_OPERATION_NOT_ALLOWED"
        const val ERROR_REQUIRES_RECENT_LOGIN = "ERROR_REQUIRES_RECENT_LOGIN"
        const val ERROR_USER_DISABLED =  "ERROR_USER_DISABLED"
        const val ERROR_USER_MISMATCH = "ERROR_USER_MISMATCH"
        const val ERROR_USER_NOT_FOUND = "ERROR_USER_NOT_FOUND"
        const val ERROR_USER_TOKEN_EXPIRED = "ERROR_USER_TOKEN_EXPIRED"
        const val ERROR_WEAK_PASSWORD = "ERROR_WEAK_PASSWORD"
        const val ERROR_WRONG_PASSWORD = "ERROR_WRONG_PASSWORD"
    }
}