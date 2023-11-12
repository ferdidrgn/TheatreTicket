package com.ferdidrgn.theatreticket.ui.otp

import android.os.Bundle
import androidx.activity.viewModels
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseActivity
import com.ferdidrgn.theatreticket.base.BasePopUp
import com.ferdidrgn.theatreticket.databinding.ActivityOtpactivityBinding
import com.ferdidrgn.theatreticket.enums.WhichEditProfile
import com.ferdidrgn.theatreticket.tools.NavHandler
import com.ferdidrgn.theatreticket.tools.builderADS
import com.ferdidrgn.theatreticket.tools.ioScope
import com.ferdidrgn.theatreticket.tools.mainScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class OTPActivity : BaseActivity<OTPViewModel, ActivityOtpactivityBinding>() {

    override fun getVM(): Lazy<OTPViewModel> = viewModels()

    override fun getDataBinding(): ActivityOtpactivityBinding =
        ActivityOtpactivityBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstance: Bundle?) {
        binding.viewModel = viewModel
        builderADS(this, binding.adView)
        observe()
    }

    private fun observe() {
        viewModel.sendOtp.observe(this) { sendOtp ->
            if (sendOtp == true) {
                viewModel.startTime(this, false)
                viewModel.sendOtp(this, false)
            }
        }

        viewModel.goToUserProfile.observe(this) { goToUserProfile ->
            if (goToUserProfile == true)
                NavHandler.instance.toEditProfileActivity(this, WhichEditProfile.LogIn)
        }

        ioScope {
            viewModel.timerFinished.collectLatest { status ->
                mainScope {
                    if (status) {
                        showResendCodeDialog()
                    }
                }
            }
        }
    }

    private fun showResendCodeDialog() {
        val pupUp = BasePopUp()
        pupUp.apply {
            setPositiveText(this.getString(R.string.resend_code))
            setNegativeText(this.getString(R.string.cancel))
            setDesc(this.getString(R.string.resend_code))
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