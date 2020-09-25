package com.duke.elliot.kim.kotlin.youbroker.sign_in

import com.duke.elliot.kim.kotlin.youbroker.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import timber.log.Timber

object SignInHelper {
    const val REQUEST_CODE_GOOGLE_SIGN_IN = 1000

    /* TODO Move to SignInFragment
    private fun signInWithEmail() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                    Timber.d("Email sign in successful")
                else
                    firebaseExceptionHandling(task.exception as FirebaseException)
            }
    }
     */

    fun signInWithGoogle(signInFragment: SignInFragment) {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(signInFragment.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(signInFragment.requireContext(), googleSignInOptions)
        val signInIntent = googleSignInClient?.signInIntent

        signInFragment.startActivityForResult(signInIntent, REQUEST_CODE_GOOGLE_SIGN_IN)
    }

    fun firebaseAuthWithGoogle(signInFragment: SignInFragment, account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful)
                Timber.d("Google sign in successful")
            else
                signInFragment.getFirebaseExceptionHandler()?.handleException(task.exception as FirebaseException)
        }
    }

    /* TODO Implementation
    private fun signInWithFacebook() {
        LoginManager.getInstance().loginBehavior = LoginBehavior.NATIVE_WITH_FALLBACK
        LoginManager.getInstance()
            .logInWithReadPermissions(activity, listOf("public_profile", "email"))
        LoginManager.getInstance().registerCallback((activity as MainActivity).callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    firebaseAuthWithFacebook(result)
                }

                override fun onCancel() {
                    Timber.e("Facebook sign in canceled")
                }

                override fun onError(error: FacebookException?) {
                    showToast(requireContext(), getString(R.string.failed_to_sign_in_with_facebook))
                    error?.printStackTrace()
                }
            })
    }

    private fun firebaseAuthWithFacebook(result: LoginResult?) {
        val credential = FacebookAuthProvider.getCredential(result?.accessToken?.token!!)
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful)
                Timber.d("Facebook sign in successful")
            else
                firebaseExceptionHandling(task.exception!! as FirebaseException)
        }
    }

    private fun signInWithTwitter() {
        val provider = OAuthProvider.newBuilder("twitter.com")
        val pendingResultTask = (requireActivity() as MainActivity).firebaseAuth.pendingAuthResult
        if (pendingResultTask != null) {
            pendingResultTask
                .addOnSuccessListener(
                    OnSuccessListener { authResult ->
                        // IdP Data: authResult.getAdditionalUserInfo().getProfile()
                        // OAuth Access Token: authResult.getCredential().getAccessToken()
                        // OAuth Secret: authResult.getCredential().getSecret()
                        if (authResult != null)
                            firebaseAuthWithTwitter(authResult)
                        else {
                            showToast(requireContext(), getString(R.string.failed_to_sign_in_with_twitter))
                            Timber.e("authResult is null")
                        }
                    })
                .addOnFailureListener{
                    showToast(requireContext(), getString(R.string.failed_to_sign_in_with_twitter))
                    it.printStackTrace()
                }
        } else {
            (requireActivity() as MainActivity).firebaseAuth
                .startActivityForSignInWithProvider(requireActivity(), provider.build())
                .addOnSuccessListener{ authResult ->
                    firebaseAuthWithTwitter(authResult)
                }
                .addOnFailureListener {
                    showToast(requireContext(), getString(R.string.failed_to_sign_in_with_twitter))
                    it.printStackTrace()
                }
        }
    }

    private fun firebaseAuthWithTwitter(result: AuthResult) {
        val credential = result.credential
        if (credential != null) {
            FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener { task ->
                if (task.isSuccessful)
                    Timber.d("Twitter sign in successful")
                else {
                    firebaseExceptionHandling(task.exception!! as FirebaseException)
                }
            }
        } else {
            showToast(requireContext(), getString(R.string.failed_to_sign_in_with_twitter))
            Timber.e("credential is null")
        }
    }
     */
}