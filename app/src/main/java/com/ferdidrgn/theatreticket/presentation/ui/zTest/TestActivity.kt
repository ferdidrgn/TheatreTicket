package com.ferdidrgn.theatreticket.presentation.ui.zTest

import android.os.Bundle
import androidx.activity.viewModels
import com.ferdidrgn.theatreticket.base.BaseActivity
import com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData.Seat
import com.ferdidrgn.theatreticket.databinding.ActivityTestBinding
import com.ferdidrgn.theatreticket.tools.components.customSeat.CustomSeatRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestActivity : BaseActivity<TestViewModel, ActivityTestBinding>() {

    override fun getVM(): Lazy<TestViewModel> = viewModels()

    override fun getDataBinding(): ActivityTestBinding = ActivityTestBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstance: Bundle?) {
        binding.viewModel = viewModel

    }

}