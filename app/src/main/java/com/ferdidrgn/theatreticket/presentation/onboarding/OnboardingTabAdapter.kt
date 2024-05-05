package com.ferdidrgn.theatreticket.presentation.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ferdidrgn.theatreticket.presentation.onboarding.onboardingFragments.OnboardingFirstFragment
import com.ferdidrgn.theatreticket.presentation.onboarding.onboardingFragments.OnboardingSecondFragment
import com.ferdidrgn.theatreticket.presentation.onboarding.onboardingFragments.OnboardingThirdFragment

class OnboardingTabAdapter(fragmentManager: FragmentActivity) :
    FragmentStateAdapter(fragmentManager) {
    override fun getItemCount(): Int = 3
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OnboardingFirstFragment()
            1 -> OnboardingSecondFragment()
            2 -> OnboardingThirdFragment()
            else -> OnboardingFirstFragment()
        }
    }
}