package com.ferdidrgn.theatreticket.presentation.language

import android.os.Bundle
import androidx.activity.viewModels
import com.ferdidrgn.theatreticket.util.base.BaseActivity
import com.ferdidrgn.theatreticket.databinding.ActivityLanguageBinding
import com.ferdidrgn.theatreticket.util.NavHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LanguageActivity : BaseActivity<LanguageViewModel, ActivityLanguageBinding>() {
    override fun getVM(): Lazy<LanguageViewModel> = viewModels()

    override fun getDataBinding(): ActivityLanguageBinding =
        ActivityLanguageBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstance: Bundle?) {
        binding.viewModel = viewModel
        viewModel.firstState()
        setAds(binding.adView)
        observeEvents()
        binding.customToolbar.backIconOnBackPress(this)
    }

    private fun observeEvents() {
        viewModel.selected = {  NavHandler.instance.toMainActivity(this) }
    }

    override fun onResume() {
        super.onResume()
        setAds(binding.adView)
    }
}