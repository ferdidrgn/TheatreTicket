package com.ferdidrgn.theatreticket.ui.onbarding

import android.os.Bundle
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseActivity
import com.ferdidrgn.theatreticket.databinding.ActivityOnboardingBinding
import com.ferdidrgn.theatreticket.tools.NavHandler
import com.ferdidrgn.theatreticket.tools.hide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingActivity : BaseActivity<OnboardingViewModel, ActivityOnboardingBinding>() {

    private lateinit var pageChangeCallback: ViewPager2.OnPageChangeCallback
    private var lastPage = false

    override fun getVM(): Lazy<OnboardingViewModel> = viewModels()

    override fun getDataBinding(): ActivityOnboardingBinding =
        ActivityOnboardingBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstance: Bundle?) {
        binding.viewModel = viewModel
        viewPagerSet()
        clickerListener()
    }

    private fun viewPagerSet() {
        val adapter = OnboardingTabAdapter(this)

        pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.dotsIndicator.selection = position
                if (position == 2) {
                    lastPage = true
                    binding.tvSkip.hide()
                    binding.rlNext.hide()
                    binding.tvNext.text = getString(R.string.get_started)
                }
            }
        }

        binding.vpSlider.registerOnPageChangeCallback(pageChangeCallback)
    }

    private fun clickerListener() {
        binding.tvSkip.setOnClickListener {
            //NavHandler.instance.loginActivity(this)
        }

        binding.rlNext.setOnClickListener {
            if (lastPage.not()) {
                binding.vpSlider.currentItem++
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.vpSlider.unregisterOnPageChangeCallback(pageChangeCallback)
    }
}