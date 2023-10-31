package com.ferdidrgn.theatreticket.ui.login

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.ferdidrgn.theatreticket.base.BaseActivity
import com.ferdidrgn.theatreticket.databinding.ActivityLoginBinding
import com.ferdidrgn.theatreticket.repository.UserFirebaseQueries
import com.ferdidrgn.theatreticket.tools.ClientPreferences
import com.ferdidrgn.theatreticket.tools.NavHandler
import com.ferdidrgn.theatreticket.tools.onClickDelayed
import com.ferdidrgn.theatreticket.tools.showToast
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<LoginViewModel, ActivityLoginBinding>() {

    private lateinit var auth: FirebaseAuth
    private var oneTapClient = Identity.getSignInClient(this)
    private val REQ_ONE_TAP = 2  // Activity'ye özgü herhangi bir tamsayı olabilir

    override fun getVM(): Lazy<LoginViewModel> = viewModels()

    override fun getDataBinding(): ActivityLoginBinding =
        ActivityLoginBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstance: Bundle?) {
        binding.viewModel = viewModel

        auth = Firebase.auth

        binding.tvGoogle.onClickDelayed {
            signIn()
        }

    }

    override fun onStart() {
        super.onStart()
        // Kullanıcı oturum açmış mı (null olmayan) kontrol eder ve UI'yı buna göre günceller.
        //Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQ_ONE_TAP -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken
                    when {
                        idToken != null -> {
                            // Google'dan bir kimlik belirteci alındı. Bu, Firebase ile kimlik doğrulamak için kullanılabilir.
                            Log.d(TAG, "Get Token")
                            ClientPreferences.inst.token = idToken
                            val userFirebaseQueries = UserFirebaseQueries()
                            userFirebaseQueries.currentUserDetails()?.update("token", idToken)
                        }
                        else -> {
                            Log.d(TAG, "Ain't Token")
                        }
                    }
                } catch (e: ApiException) {
                    showToast("${e.message}")
                }
            }
        }

        val googleCredential = oneTapClient.getSignInCredentialFromIntent(data)
        val idToken = googleCredential.googleIdToken
        when {
            idToken != null -> {
                // Google'dan bir kimlik belirteci alındı. Bu, Firebase ile kimlik doğrulamak için kullanılabilir.
                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                auth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            ClientPreferences.inst.token = idToken
                            Log.d(TAG, "Success Google Login")
                            val user = auth.currentUser
                            updateUI(user)
                        } else {
                            Log.w(TAG, "UnSuccess Google Login", task.exception)
                            updateUI(null)
                        }
                    }
            }
            else -> {
                Log.d(TAG, "Error")
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            NavHandler.instance.toMainActivityFinishAffinity(this)
        } else {
            recreate() //re-create activity
        }
    }

    private fun signIn() {
        val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Sunucunuzun istemci kimliği, Android istemci kimliği değil.
                    .setServerClientId("web_id")
                    // Daha önce oturum açmak için kullanılan hesapları göster
                    //Show previously used accounts for sign-in.
                    .setFilterByAuthorizedAccounts(true)
                    .build()
            )
            .build()

        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(this) { result ->
                try {
                    startIntentSenderForResult(
                        result.pendingIntent.intentSender,
                        REQ_ONE_TAP,
                        null,
                        0,
                        0,
                        0,
                        null
                    )
                } catch (e: IntentSender.SendIntentException) {
                    showToast("${e.message}")
                }
            }
            .addOnFailureListener(this) { e ->
                showToast("${e.message}")
            }
    }
}