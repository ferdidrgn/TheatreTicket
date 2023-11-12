package com.ferdidrgn.theatreticket.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat.getString
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseActivity
import com.ferdidrgn.theatreticket.databinding.ActivityLoginBinding
import com.ferdidrgn.theatreticket.enums.Roles
import com.ferdidrgn.theatreticket.enums.WhichEditProfile
import com.ferdidrgn.theatreticket.tools.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<LoginViewModel, ActivityLoginBinding>() {

    private lateinit var auth: FirebaseAuth

    override fun getVM(): Lazy<LoginViewModel> = viewModels()

    override fun getDataBinding(): ActivityLoginBinding =
        ActivityLoginBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstance: Bundle?) {
        binding.viewModel = viewModel
        auth = FirebaseAuth.getInstance()
        builderADS(this, binding.adView)
        checkLandingOrEnter(getIntentObserve())
        clickEvents()
    }

    private fun clickEvents() {

        viewModel.btnSignInGoogle.observe(this) {
            sigIn()
        }
        viewModel.btnSignInPhoneNumber.observe(this) {
            NavHandler.instance.toOTPActivity(this)
        }

        viewModel.btnGuest.observe(this) {
            NavHandler.instance.toMainActivityFinishAffinity(this)
            ClientPreferences.inst.role = Roles.Guest.role
            finish()
        }
    }

    private fun checkLandingOrEnter(isSettingsClickedLogIn: Boolean) {
        if (ClientPreferences.inst.isFirstLaunch == true)
            NavHandler.instance.toOnboardingActivity(this)
        else {
            isUserLogInAndFillInfo(isSettingsClickedLogIn)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                showToast(getString(R.string.error_google_sign_in) + "${e.message}")
            }
        }
    }

    private fun getIntentObserve(): Boolean {
        return intent.getBooleanExtra(IS_SETTINGS_CLICKED_LOG_IN, false)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser

                    setClientPreferneces(user)
                    showToast(getString(R.string.signed_in_as) + " ${user?.displayName}")
                    NavHandler.instance.toEditProfileActivity(this, WhichEditProfile.LogIn)
                    finishAffinity()
                } else
                    showToast(getString(R.string.error_auth_failed))
            }
    }

    private fun isUserLogInAndFillInfo(isSettingsClickedLogIn: Boolean) {

        if (auth.currentUser != null && ClientPreferences.inst.isBlankUserInfo == true) {
            NavHandler.instance.toEditProfileActivity(this, WhichEditProfile.LogIn)
            finishAffinity()
        }
        // The user is already signed in, navigate to MainActivity
        else if (auth.currentUser != null || (!isSettingsClickedLogIn && ClientPreferences.inst.role != null)) {
            NavHandler.instance.toMainActivityFinishAffinity(this)
            finish()
        }

    }

    private fun sigIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(this@LoginActivity, R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun setClientPreferneces(user: FirebaseUser?) {
        ClientPreferences.inst.apply {
            role = Roles.User.role
            userID = user?.uid
            userFullName = user?.displayName
            userEmail = user?.email
            userPhone = user?.phoneNumber?.removeWhiteSpace()
            userPhotoUrl = user?.photoUrl.toString()
            isGoogleSignIn = true
            isPhoneNumberSignIn = false
        }
        //token = user?.getIdToken(true)?.result?.token.toString()
    }
}