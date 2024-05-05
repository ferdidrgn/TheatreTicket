package com.ferdidrgn.theatreticket.presentation.onboarding

import android.os.Bundle
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.ferdidrgn.theatreticket.util.base.BaseActivity
import com.ferdidrgn.theatreticket.databinding.ActivityOnboardingBinding
import com.ferdidrgn.theatreticket.util.ClientPreferences
import com.ferdidrgn.theatreticket.util.NavHandler
import com.ferdidrgn.theatreticket.util.hide
import com.ferdidrgn.theatreticket.util.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingActivity : BaseActivity<OnboardingViewModel, ActivityOnboardingBinding>() {

    private lateinit var pageChangeCallback: ViewPager2.OnPageChangeCallback
    private var lastPage = false
    private var firstPage = false

    override fun getVM(): Lazy<OnboardingViewModel> = viewModels()

    override fun getDataBinding(): ActivityOnboardingBinding =
        ActivityOnboardingBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstance: Bundle?) {
        binding.viewModel = viewModel
        viewPagerSet()
        observe()
    }

    private fun viewPagerSet() {
        val adapter = OnboardingTabAdapter(this)
        binding.vpSlider.adapter = adapter

        pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.dotsIndicator.selection = position
                when (position) {
                    0 -> {
                        firstPage = true
                        binding.rlStarted.hide()
                    }

                    2 -> {
                        lastPage = true
                        binding.rlStarted.show()
                    }

                    else -> binding.rlStarted.hide()
                }
            }
        }

        binding.vpSlider.registerOnPageChangeCallback(pageChangeCallback)
    }

    private fun observe() {
        viewModel.isPageIndicatorNext.observe(this) { isIndicatorNext ->
            if (isIndicatorNext == true) {
                binding.vpSlider.currentItem++
                if (lastPage.not())
                    binding.vpSlider.currentItem++
            }
            if (isIndicatorNext == false) {
                binding.vpSlider.currentItem--
                if (firstPage.not())
                    binding.vpSlider.currentItem--
            }
        }

        viewModel.getLoginActivity.observe(this) {
            ClientPreferences.inst.isFirstLaunch = false
            NavHandler.instance.toLoginActivity(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.vpSlider.unregisterOnPageChangeCallback(pageChangeCallback)
    }
}