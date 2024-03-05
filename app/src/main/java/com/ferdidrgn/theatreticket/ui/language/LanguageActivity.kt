package com.ferdidrgn.theatreticket.ui.language

import android.os.Bundle
import androidx.activity.viewModels
import com.ferdidrgn.theatreticket.base.BaseActivity
import com.ferdidrgn.theatreticket.databinding.ActivityLanguageBinding
import com.ferdidrgn.theatreticket.tools.NavHandler
import com.ferdidrgn.theatreticket.tools.builderADS
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LanguageActivity : BaseActivity<LanguageViewModel, ActivityLanguageBinding>() {
    override fun getVM(): Lazy<LanguageViewModel> = viewModels()

    override fun getDataBinding(): ActivityLanguageBinding =
        ActivityLanguageBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstance: Bundle?) {
        binding.viewModel = viewModel
        viewModel.firstState()
        builderADS(this, binding.adView)
        observeEvents()
        binding.customToolbar.backIconOnBackPress(this)
    }

    private fun observeEvents() {
        viewModel.selected = {
            restart()
        }

    }
    private fun restart() {
        NavHandler.instance.toMainActivityFinishAffinity(this)
        finishAffinity()
    }
}