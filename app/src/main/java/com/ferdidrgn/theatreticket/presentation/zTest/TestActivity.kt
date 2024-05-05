package com.ferdidrgn.theatreticket.presentation.zTest

import android.os.Bundle
import androidx.activity.viewModels
import com.ferdidrgn.theatreticket.util.base.BaseActivity
import com.ferdidrgn.theatreticket.databinding.ActivityTestBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestActivity : BaseActivity<TestViewModel, ActivityTestBinding>() {

    override fun getVM(): Lazy<TestViewModel> = viewModels()

    override fun getDataBinding(): ActivityTestBinding = ActivityTestBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstance: Bundle?) {
        binding.viewModel = viewModel

    }

}