package com.ferdidrgn.theatreticket.presentation.ui.onboarding.onboardingFragments

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.ferdidrgn.theatreticket.base.BaseFragment
import com.ferdidrgn.theatreticket.databinding.FragmentOnboardingThirdBinding
import com.ferdidrgn.theatreticket.tools.enums.WhichTermsAndPrivacy
import com.ferdidrgn.theatreticket.tools.NavHandler
import com.ferdidrgn.theatreticket.tools.builderADS
import com.ferdidrgn.theatreticket.ui.onboarding.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingThirdFragment :
    BaseFragment<OnboardingViewModel, FragmentOnboardingThirdBinding>() {
    override fun getVM(): Lazy<OnboardingViewModel> = viewModels()

    override fun getDataBinding(): FragmentOnboardingThirdBinding =
        FragmentOnboardingThirdBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        builderADS(requireContext(), binding.adView)

        viewModel.getTermsConditionActivity.observe(viewLifecycleOwner) {
            NavHandler.instance.toTermsConditionsAndPrivacyPolicyActivity(
                requireContext(),
                WhichTermsAndPrivacy.TermsAndCondition
            )
        }
    }
}