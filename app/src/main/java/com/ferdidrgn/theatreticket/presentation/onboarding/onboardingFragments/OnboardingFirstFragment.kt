package com.ferdidrgn.theatreticket.presentation.onboarding.onboardingFragments

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.ferdidrgn.theatreticket.util.base.BaseFragment
import com.ferdidrgn.theatreticket.databinding.FragmentOnboardingFirstBinding
import com.ferdidrgn.theatreticket.presentation.onboarding.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFirstFragment :
    BaseFragment<OnboardingViewModel, FragmentOnboardingFirstBinding>() {
    override fun getVM(): Lazy<OnboardingViewModel> = viewModels()

    override fun getDataBinding(): FragmentOnboardingFirstBinding =
        FragmentOnboardingFirstBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstanceState: Bundle?) {
        setAds(binding.adView)
    }

    override fun onResume() {
        super.onResume()
        setAds(binding.adView)
    }

}