package com.duke.elliot.kim.kotlin.youbroker.sign_in

import androidx.appcompat.app.AlertDialog
import com.duke.elliot.kim.kotlin.youbroker.MainActivity
import com.duke.elliot.kim.kotlin.youbroker.R
import com.duke.elliot.kim.kotlin.youbroker.utility.getProgressDialog
import com.duke.elliot.kim.kotlin.youbroker.utility.showToast
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginBehavior
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import timber.log.Timber

class SignInHelper(private val signInFragment: SignInFragment,
                   private val firebaseExceptionHandler: FirebaseExceptionHandler) {

    private val activity = signInFragment.requireActivity() as MainActivity
    private val context = signInFragment.requireContext()
    private val progressBar: AlertDialog = getProgressDialog(signInFragment.requireContext())

    private fun getString(resId: Int) = context.getString(resId)
    private fun commonSignInEvent() {
        activity.onBackPressed()
        showToast(context, getString(R.string.signed_in))
    }

    fun signInWithEmail(email: String, password: String) {
        showProgressBar()
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                    Timber.d("Email sign in successful")
                else
                    firebaseExceptionHandler.handleException(task.exception as FirebaseException)
            }
    }

    fun signInWithFacebook() {
        showProgressBar()
        LoginManager.getInstance().loginBehavior = LoginBehavior.NATIVE_WITH_FALLBACK
        LoginManager.getInstance().logInWithReadPermissions(activity, listOf("public_profile", "email"))
        LoginManager.getInstance().registerCallback(activity.callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    firebaseAuthWithFacebook(result)
                }

                override fun onCancel() {
                    dismissProgressBar()
                    Timber.e("Facebook sign in canceled")
                }

                override fun onError(error: FacebookException?) {
                    dismissProgressBar()
                    showToast(context, "${getString(R.string.failed_to_sign_in_with_facebook)} \n${error?.message}")
                    error?.printStackTrace()
                }
            })
    }

    fun firebaseAuthWithFacebook(result: LoginResult?) {
        val credential = FacebookAuthProvider.getCredential(result?.accessToken?.token!!)
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Timber.d("Facebook sign in successful")
                commonSignInEvent()
            } else
                firebaseExceptionHandler.handleException(task.exception as FirebaseException)

            dismissProgressBar()
        }
    }

    fun signInWithGoogle() {
        showProgressBar()
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions)
        val signInIntent = googleSignInClient?.signInIntent

        signInFragment.startActivityForResult(signInIntent, REQUEST_CODE_GOOGLE_SIGN_IN)
    }

    fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Timber.d("Google sign in successful")
                commonSignInEvent()
            } else
                firebaseExceptionHandler.handleException(task.exception as FirebaseException)

            dismissProgressBar()
        }
    }

    fun signInWithTwitter() {
        showProgressBar()
        (signInFragment.requireActivity() as MainActivity).getTwitterAuthClient()
            .authorize(signInFragment.requireActivity(), object : Callback<TwitterSession>() {
                override fun success(result: Result<TwitterSession>?) {
                    result?.data?.let { firebaseAuthWithTwitter(it) }
                }

                override fun failure(exception: TwitterException?) {
                    dismissProgressBar()
                    showToast(context, "${getString(R.string.failed_to_sign_in_with_twitter)} \n${exception?.message}")
                }
            })
    }

    private fun firebaseAuthWithTwitter(session: TwitterSession) {
        val credential = TwitterAuthProvider.getCredential(
            session.authToken.token,
            session.authToken.secret
        )
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Timber.d("Twitter sign in successful")
                commonSignInEvent()
            }
            else
                firebaseExceptionHandler.handleException(task.exception as FirebaseException)

            dismissProgressBar()
        }
    }

    fun showProgressBar() {
        progressBar.show()
    }

    fun dismissProgressBar() {
        progressBar.dismiss()
    }

    companion object {
        const val REQUEST_CODE_GOOGLE_SIGN_IN = 1000
    }
}