package com.ferdidrgn.theatreticket.presentation.onboarding.onboardingFragments

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.ferdidrgn.theatreticket.util.base.BaseFragment
import com.ferdidrgn.theatreticket.databinding.FragmentOnboardingSecondBinding
import com.ferdidrgn.theatreticket.presentation.onboarding.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingSecondFragment :
    BaseFragment<OnboardingViewModel, FragmentOnboardingSecondBinding>() {
    override fun getVM(): Lazy<OnboardingViewModel> = viewModels()

    override fun getDataBinding(): FragmentOnboardingSecondBinding =
        FragmentOnboardingSecondBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstanceState: Bundle?) {
        setAds(binding.adView)
    }

    override fun onResume() {
        super.onResume()
        setAds(binding.adView)
    }
}