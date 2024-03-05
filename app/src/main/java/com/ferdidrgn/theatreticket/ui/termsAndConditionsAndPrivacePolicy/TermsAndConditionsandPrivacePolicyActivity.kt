package com.ferdidrgn.theatreticket.ui.termsAndConditionsAndPrivacePolicy

import android.os.Bundle
import androidx.activity.viewModels
import com.ferdidrgn.theatreticket.base.BaseActivity
import com.ferdidrgn.theatreticket.databinding.ActivityTermsConditionsAndPrivacePolicyBinding
import com.ferdidrgn.theatreticket.enums.WhichTermsAndPrivace
import com.ferdidrgn.theatreticket.tools.WHICH_TERMS_PRIVACE
import com.ferdidrgn.theatreticket.tools.builderADS
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TermsAndConditionsandPrivacePolicyActivity :
    BaseActivity<TermsConditionsAndPrivacePolicyViewModel, ActivityTermsConditionsAndPrivacePolicyBinding>() {

    override fun getVM(): Lazy<TermsConditionsAndPrivacePolicyViewModel> = viewModels()

    override fun getDataBinding(): ActivityTermsConditionsAndPrivacePolicyBinding =
        ActivityTermsConditionsAndPrivacePolicyBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstance: Bundle?) {
        binding.viewModel = viewModel
        builderADS(this, binding.adView)
        binding.customToolbar.backIconOnBackPress(this)
        observe()
    }


    private fun observe() {
        val type = intent.getSerializableExtra(WHICH_TERMS_PRIVACE)
        viewModel.getHtmlFromUrl(type as WhichTermsAndPrivace)
    }
}