package com.ferdidrgn.theatreticket.ui.onboarding.onboardingFragments

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.ferdidrgn.theatreticket.base.BaseFragment
import com.ferdidrgn.theatreticket.databinding.FragmentOnboardingFirstBinding
import com.ferdidrgn.theatreticket.tools.builderADS
import com.ferdidrgn.theatreticket.ui.onboarding.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFirstFragment :
    BaseFragment<OnboardingViewModel, FragmentOnboardingFirstBinding>() {
    override fun getVM(): Lazy<OnboardingViewModel> = viewModels()

    override fun getDataBinding(): FragmentOnboardingFirstBinding =
        FragmentOnboardingFirstBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstanceState: Bundle?) {
        builderADS(requireActivity(), binding.adView)
    }

}