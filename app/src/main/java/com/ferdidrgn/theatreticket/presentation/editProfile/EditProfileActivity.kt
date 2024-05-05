package com.ferdidrgn.theatreticket.presentation.editProfile

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.util.base.BaseActivity
import com.ferdidrgn.theatreticket.util.base.BasePopUp
import com.ferdidrgn.theatreticket.databinding.ActivityEditProfileBinding
import com.ferdidrgn.theatreticket.util.ClientPreferences
import com.ferdidrgn.theatreticket.util.IMAGE_REQUEST_CODE
import com.ferdidrgn.theatreticket.util.NavHandler
import com.ferdidrgn.theatreticket.util.WHICH_EDIT_PROFILE
import com.ferdidrgn.theatreticket.util.WhichEditProfile
import com.ferdidrgn.theatreticket.util.showToast
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
        setAds(binding.adView)
        binding.customToolbar.backIconOnBackPress(this)
        observe()
    }

    private fun observe() {

        val type = intent.getSerializableExtra(WHICH_EDIT_PROFILE) as WhichEditProfile
        viewModel.whichComeAction.value = type
        viewModel.changeToolbarText()

        viewModel.btnCstmDatePickerClick.observe(this) {
            binding.cdpAge.setCustomDataPickerClick()
        }

        viewModel.btnUpdateAccClick.observe(this) {
            updateOrDeleteInformationPopUp(this, true)
        }

        viewModel.btnDeleteAccountClick.observe(this) {
            updateOrDeleteInformationPopUp(this, false)
        }

        viewModel.logOut.observe(this) {
            logout()
        }

        viewModel.imagePermission.observe(this) {
            permissionCheck()
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage != null)
                errorOrSuccessMessagePopUp(this, errorMessage, false)
        }

        viewModel.successMessage.observe(this) { successMessage ->
            if (successMessage != null)
                errorOrSuccessMessagePopUp(this, successMessage, true)
        }

    }

    override fun onResume() {
        super.onResume()
        setAds(binding.adView)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            binding.imgProfil.setImageURI(data?.data)
            viewModel.addOrUpdateImgUrl.value = data?.data
            viewModel.imageUrl.value = data?.data.toString()
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

    private fun errorOrSuccessMessagePopUp(context: Context, message: String, isSuccess: Boolean) {
        val pupUp = BasePopUp(isSuccess = isSuccess)
        pupUp.apply {
            setPositiveText(context.getString(R.string.done))
            setDesc(message)
            setOnPositiveClick {
                if (isSuccess)
                    NavHandler.instance.toMainActivity(
                        this@EditProfileActivity,
                        finishAffinity = true
                    )
                dismiss()
            }
        }
        pupUp.show(supportFragmentManager, "")
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

    private fun permissionCheck() {
        val readImagePermission =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                android.Manifest.permission.READ_MEDIA_IMAGES else android.Manifest.permission.READ_EXTERNAL_STORAGE
        requestPermissionLauncher.launch(readImagePermission)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted)
                pickImageFromGallery()
            else
                showToast(getString(R.string.permission_denied))
        }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }
}