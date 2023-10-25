package com.ferdidrgn.theatreticket.ui.onbarding

import android.os.Bundle
import androidx.activity.viewModels
import com.ferdidrgn.theatreticket.base.BaseActivity
import com.ferdidrgn.theatreticket.databinding.ActivityOnboardingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingActivity : BaseActivity<OnboardingViewModel, ActivityOnboardingBinding>() {

    override fun getVM(): Lazy<OnboardingViewModel> = viewModels()

    override fun getDataBinding(): ActivityOnboardingBinding =
        ActivityOnboardingBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstance: Bundle?) {
        binding.viewModel = viewModel
    }
}