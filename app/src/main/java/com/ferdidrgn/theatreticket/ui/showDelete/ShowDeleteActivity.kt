package com.ferdidrgn.theatreticket.ui.showDelete

import  android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseActivity
import com.ferdidrgn.theatreticket.base.BasePopUp
import com.ferdidrgn.theatreticket.databinding.ActivityShowDeleteBinding
import com.ferdidrgn.theatreticket.ui.main.home.ShowsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowDeleteActivity : BaseActivity<ShowDeleteViewModel, ActivityShowDeleteBinding>() {
    override fun getVM(): Lazy<ShowDeleteViewModel> = viewModels()

    override fun getDataBinding(): ActivityShowDeleteBinding =
        ActivityShowDeleteBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstance: Bundle?) {
        binding.viewModel = viewModel
        binding.showAdapter = ShowsAdapter(viewModel)

        observe()

        viewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage != null)
                messagePopUp(this, errorMessage, false)
        }
    }

    private fun observe() {
        viewModel.getAllShow()
    }

    private fun messagePopUp(context: Context, message: String, isSuccess: Boolean) {
        val pupUp = BasePopUp(isSuccess = isSuccess)
        pupUp.apply {
            setPositiveText(context.getString(R.string.done))
            setDesc(message)
            setOnPositiveClick {
                dismiss()
            }
        }
        pupUp.show(supportFragmentManager, "")
    }
}