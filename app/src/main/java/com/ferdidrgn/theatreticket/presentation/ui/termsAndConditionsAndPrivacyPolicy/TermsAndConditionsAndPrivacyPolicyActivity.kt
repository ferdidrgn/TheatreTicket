package com.ferdidrgn.theatreticket.presentation.ui.termsAndConditionsAndPrivacyPolicy

import android.os.Bundle
import androidx.activity.viewModels
import com.ferdidrgn.theatreticket.base.BaseActivity
import com.ferdidrgn.theatreticket.databinding.ActivityTermsConditionsAndPrivacyPolicyBinding
import com.ferdidrgn.theatreticket.tools.enums.WhichTermsAndPrivacy
import com.ferdidrgn.theatreticket.tools.WHICH_TERMS_PRIVACY
import com.ferdidrgn.theatreticket.tools.builderADS
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TermsAndConditionsAndPrivacyPolicyActivity :
    BaseActivity<TermsConditionsAndPrivacyPolicyViewModel, ActivityTermsConditionsAndPrivacyPolicyBinding>() {

    override fun getVM(): Lazy<TermsConditionsAndPrivacyPolicyViewModel> = viewModels()

    override fun getDataBinding(): ActivityTermsConditionsAndPrivacyPolicyBinding =
        ActivityTermsConditionsAndPrivacyPolicyBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstance: Bundle?) {
        binding.viewModel = viewModel
        setAds(binding.adView)
        binding.customToolbar.backIconOnBackPress(this)
        observe()
    }


    private fun observe() {
        val type = intent.getSerializableExtra(WHICH_TERMS_PRIVACY)
        viewModel.getHtmlFromUrl(type as WhichTermsAndPrivacy)
    }

    override fun onResume() {
        super.onResume()
        setAds(binding.adView)
    }
}