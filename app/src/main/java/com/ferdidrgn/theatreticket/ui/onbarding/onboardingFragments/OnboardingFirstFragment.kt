package com.ferdidrgn.theatreticket.ui.onbarding.onboardingFragments

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.ferdidrgn.theatreticket.base.BaseFragment
import com.ferdidrgn.theatreticket.databinding.FragmentOnboardingFirstBinding
import com.ferdidrgn.theatreticket.ui.onbarding.OnboardingViewModel

class OnboardingFirstFragment :
    BaseFragment<OnboardingViewModel, FragmentOnboardingFirstBinding>() {
    override fun getVM(): Lazy<OnboardingViewModel> = viewModels()

    override fun getDataBinding(): FragmentOnboardingFirstBinding =
        FragmentOnboardingFirstBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstanceState: Bundle?) {
        binding.testText.text = "Test"
    }

}