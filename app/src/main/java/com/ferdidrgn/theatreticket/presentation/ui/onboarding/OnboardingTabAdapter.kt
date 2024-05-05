package com.ferdidrgn.theatreticket.presentation.ui.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ferdidrgn.theatreticket.ui.onboarding.onboardingFragments.OnboardingFirstFragment
import com.ferdidrgn.theatreticket.ui.onboarding.onboardingFragments.OnboardingSecondFragment
import com.ferdidrgn.theatreticket.ui.onboarding.onboardingFragments.OnboardingThirdFragment

class OnboardingTabAdapter(fragmentManager: FragmentActivity) :
    FragmentStateAdapter(fragmentManager) {
    override fun getItemCount(): Int = 3
    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return OnboardingFirstFragment()
            1 -> return OnboardingSecondFragment()
            2 -> return OnboardingThirdFragment()
            else -> return OnboardingFirstFragment()
        }
    }
}