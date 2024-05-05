package com.ferdidrgn.theatreticket.presentation.onboarding.onboardingFragments

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.ferdidrgn.theatreticket.util.base.BaseFragment
import com.ferdidrgn.theatreticket.databinding.FragmentOnboardingThirdBinding
import com.ferdidrgn.theatreticket.presentation.onboarding.OnboardingViewModel
import com.ferdidrgn.theatreticket.util.NavHandler
import com.ferdidrgn.theatreticket.util.WhichTermsAndPrivacy
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingThirdFragment :
    BaseFragment<OnboardingViewModel, FragmentOnboardingThirdBinding>() {
    override fun getVM(): Lazy<OnboardingViewModel> = viewModels()

    override fun getDataBinding(): FragmentOnboardingThirdBinding =
        FragmentOnboardingThirdBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        setAds(binding.adView)

        viewModel.getTermsConditionActivity.observe(viewLifecycleOwner) {
            NavHandler.instance.toTermsConditionsAndPrivacyPolicyActivity(
                requireContext(),
                WhichTermsAndPrivacy.TermsAndCondition
            )
        }
    }

    override fun onResume() {
        super.onResume()
        setAds(binding.adView)
    }
}