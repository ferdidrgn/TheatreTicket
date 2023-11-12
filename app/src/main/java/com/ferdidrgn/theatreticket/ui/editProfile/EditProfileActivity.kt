package com.ferdidrgn.theatreticket.ui.editProfile

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseActivity
import com.ferdidrgn.theatreticket.base.BasePopUp
import com.ferdidrgn.theatreticket.databinding.ActivityEditProfileBinding
import com.ferdidrgn.theatreticket.enums.WhichEditProfile
import com.ferdidrgn.theatreticket.tools.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileActivity : BaseActivity<EditProfileViewModel, ActivityEditProfileBinding>() {

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun getVM(): Lazy<EditProfileViewModel> = viewModels()

    override fun getDataBinding(): ActivityEditProfileBinding =
        ActivityEditProfileBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstance: Bundle?) {
        binding.viewModel = viewModel
        builderADS(this, binding.adView)
        binding.customToolbar.backIconOnBackPress(this)
        observe()
    }

    private fun observe() {

        val type = intent.getSerializableExtra(WHICH_EDIT_PROFILE) as WhichEditProfile
        viewModel.whichComeAction.value = type
        viewModel.changeToolbarText()

        viewModel.btnUpdateAccClick.observe(this) {
            if (it == true) updateOrDeleteInformationPopUp(this, true)
        }

        viewModel.btnDeleteAccountClick.observe(this) {
            updateOrDeleteInformationPopUp(this, false)
        }

        viewModel.logOut.observe(this) {
            if (it == true) {
                logout()
            }
        }
    }

    private fun logout() {
        FirebaseMessaging.getInstance().deleteToken()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    if (ClientPreferences.inst.isGoogleSignIn == true)
                        signOutAndStartSignInActivity()

                    FirebaseAuth.getInstance().signOut()

                    goToLoginAndFinishAffinity()
                    showToast(getString(R.string.success))
                }
            }
    }

    private fun goToLoginAndFinishAffinity() {
        NavHandler.instance.toLoginActivity(this, true)
        finishAffinity()
    }

    private fun signOutAndStartSignInActivity() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        mGoogleSignInClient.signOut().addOnCompleteListener(this) {
            goToLoginAndFinishAffinity()
        }
    }


    private fun updateOrDeleteInformationPopUp(context: Context, isUpdate: Boolean) {
        val pupUp = BasePopUp()
        pupUp.apply {
            setPositiveText(context.getString(R.string.yes))
            setNegativeText(context.getString(R.string.no))
            if (isUpdate) {
                setDesc(context.getString(R.string.are_you_serious_update))
                setOnPositiveClick {
                    viewModel.checkRequestFields()
                    dismiss()
                }
            } else {
                setDesc(context.getString(R.string.are_you_serious_delete))
                setOnPositiveClick {
                    viewModel.deleteUser()
                    dismiss()
                }
            }
            setOnNegativeClick {
                dismiss()
            }
        }
        pupUp.show(supportFragmentManager, "")
    }
}