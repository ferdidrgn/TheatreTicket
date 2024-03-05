package com.ferdidrgn.theatreticket.ui.otp

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseActivity
import com.ferdidrgn.theatreticket.base.BasePopUp
import com.ferdidrgn.theatreticket.databinding.ActivityOtpactivityBinding
import com.ferdidrgn.theatreticket.enums.WhichEditProfile
import com.ferdidrgn.theatreticket.tools.*
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class OTPActivity : BaseActivity<OTPViewModel, ActivityOtpactivityBinding>() {

    override fun getVM(): Lazy<OTPViewModel> = viewModels()

    override fun getDataBinding(): ActivityOtpactivityBinding =
        ActivityOtpactivityBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstance: Bundle?) {
        binding.viewModel = viewModel
        binding.customToolbar.backIconOnBackPress(this)
        builderADS(this, binding.adView)
        observe()
    }

    private fun observe() {
        viewModel.sendOtp.observe(this) { sendOtp ->
            if (sendOtp == true) {
                viewModel.startTime(this, false)
                viewModel.sendOtp(this, false)
            }

            binding.etCode.editTextView.addTextChangedListener(object : TextWatcher {
                val maxLength = 6
                override fun beforeTextChanged(
                    charSequence: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    charSequence: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    val currentLength = charSequence?.length ?: 0

                    if (currentLength == maxLength) {
                        binding.etCode.editTextView.hideKeyboard()
                        val credential = PhoneAuthProvider.getCredential(
                            viewModel.verificationCode.value.toString(),
                            charSequence.toString()
                        )
                        viewModel.signIn(credential, view = binding.etCode.editTextView)
                    }
                }

                override fun afterTextChanged(editable: Editable?) {
                    // İşlem yapıldıktan sonra durumu kontrol etmek için kullanılır.
                }
            })
        }

        viewModel.goToUserProfile.observe(this) { goToUserProfile ->
            if (goToUserProfile == true)
                NavHandler.instance.toEditProfileActivity(this, WhichEditProfile.LogIn)
        }

        viewModel.resendClick.observe(this) { resendClick ->
            showResendCodeDialog(this)
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

    private fun errorOrSuccessMessagePopUp(context: Context, message: String, isSuccess: Boolean) {
        val pupUp = BasePopUp(isSuccess = isSuccess)
        pupUp.apply {
            setPositiveText(context.getString(R.string.done))
            setDesc(message)
            setOnPositiveClick {
                dismiss()
            }
        }
        pupUp.show(supportFragmentManager, "")
    }

    private fun showResendCodeDialog(context: Context) {
        val pupUp = BasePopUp()
        pupUp.apply {
            setPositiveText(context.getString(R.string.resend_code))
            setNegativeText(context.getString(R.string.cancel))
            setDesc(context.getString(R.string.resend_code))
            setOnPositiveClick {
                viewModel.startTime(this@OTPActivity, true)
                viewModel.sendOtp(this@OTPActivity, false)
                dismiss()
            }
            setOnNegativeClick {
                onBackPressed()
                dismiss()
            }
        }
        pupUp.show(supportFragmentManager, "")
    }

}