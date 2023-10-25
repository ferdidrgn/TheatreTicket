package com.ferdidrgn.theatreticket.ui.onbarding.onboardingFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseFragment
import com.ferdidrgn.theatreticket.databinding.FragmentOnboardingSecondBinding
import com.ferdidrgn.theatreticket.ui.onbarding.OnboardingViewModel


class OnboardingSecondFragment :
    BaseFragment<OnboardingViewModel, FragmentOnboardingSecondBinding>() {
    override fun getVM(): Lazy<OnboardingViewModel> = viewModels()

    override fun getDataBinding(): FragmentOnboardingSecondBinding =
        FragmentOnboardingSecondBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstanceState: Bundle?) {
        binding.testText.text = "Test"
    }
}