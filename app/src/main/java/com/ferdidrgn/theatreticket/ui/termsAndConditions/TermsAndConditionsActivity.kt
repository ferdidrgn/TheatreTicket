package com.ferdidrgn.theatreticket.ui.termsAndConditions

import android.os.Bundle
import androidx.activity.viewModels
import com.ferdidrgn.theatreticket.base.BaseActivity
import com.ferdidrgn.theatreticket.databinding.ActivityTermsAndConditionsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TermsAndConditionsActivity :
    BaseActivity<TermsViewModel, ActivityTermsAndConditionsBinding>() {

    override fun getVM(): Lazy<TermsViewModel> = viewModels()

    override fun getDataBinding(): ActivityTermsAndConditionsBinding =
        ActivityTermsAndConditionsBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstance: Bundle?) {
        binding.viewModel = viewModel
        binding.customToolbar.backIconOnBackPress(this)
    }
}