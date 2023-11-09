package com.ferdidrgn.theatreticket.ui.stageOperations

import android.os.Bundle
import androidx.activity.viewModels
import com.ferdidrgn.theatreticket.base.BaseActivity
import com.ferdidrgn.theatreticket.databinding.ActivityStageOperationsBinding

class StageOperationsActivity :
    BaseActivity<StageOperationsViewModel, ActivityStageOperationsBinding>() {

    override fun getVM(): Lazy<StageOperationsViewModel> = viewModels()

    override fun getDataBinding(): ActivityStageOperationsBinding =
        ActivityStageOperationsBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstance: Bundle?) {
        binding.viewModel = viewModel
    }
}