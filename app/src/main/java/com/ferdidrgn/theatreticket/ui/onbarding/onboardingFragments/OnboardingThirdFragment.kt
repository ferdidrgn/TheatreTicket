package com.ferdidrgn.theatreticket.ui.onbarding.onboardingFragments

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.ferdidrgn.theatreticket.base.BaseFragment
import com.ferdidrgn.theatreticket.databinding.FragmentOnboardingThirdBinding
import com.ferdidrgn.theatreticket.ui.onbarding.OnboardingViewModel

class OnboardingThirdFragment :
    BaseFragment<OnboardingViewModel, FragmentOnboardingThirdBinding>() {
    override fun getVM(): Lazy<OnboardingViewModel> = viewModels()

    override fun getDataBinding(): FragmentOnboardingThirdBinding =
        FragmentOnboardingThirdBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstanceState: Bundle?) {
        binding.testText.text = "Test"
    }
}