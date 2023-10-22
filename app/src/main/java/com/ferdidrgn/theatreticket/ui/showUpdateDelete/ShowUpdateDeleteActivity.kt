package com.ferdidrgn.theatreticket.ui.showUpdateDelete


import  android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseActivity
import com.ferdidrgn.theatreticket.base.BasePopUp
import com.ferdidrgn.theatreticket.databinding.ActivityShowUpdateDeleteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowUpdateDeleteActivity :
    BaseActivity<ShowUpdateDeleteViewModel, ActivityShowUpdateDeleteBinding>() {
    override fun getVM(): Lazy<ShowUpdateDeleteViewModel> = viewModels()

    override fun getDataBinding(): ActivityShowUpdateDeleteBinding =
        ActivityShowUpdateDeleteBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstance: Bundle?) {
        binding.viewModel = viewModel
        binding.showUpdateDeleteAdapter = ShowsUpdateDeleteAdapter(viewModel)
        observe()
        binding.customToolbar.backIconOnBackPress(this)

        viewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage != null)
                messagePopUp(this, errorMessage, false)
        }

        viewModel.updatePopUp.observe(this) {
            popUp(this, true)
        }

        viewModel.deletePopUp.observe(this) {
            popUp(this, false)
        }
    }

    private fun observe() {
        viewModel.getAllShow()
    }

    private fun popUp(context: Context, isUpdate: Boolean) {
        val pupUp = BasePopUp()
        pupUp.apply {
            setPositiveText(context.getString(R.string.yes))
            setNegativeText(context.getString(R.string.no))
            if (isUpdate) {
                setDesc(context.getString(R.string.are_you_serious_update))
                setOnPositiveClick {
                    // viewModel.updateShow()
                    dismiss()
                }
            } else {
                setDesc(context.getString(R.string.are_you_serious_delete))
                setOnPositiveClick {
                    viewModel.deleteShow()
                    dismiss()
                }
            }
            setOnNegativeClick {
                dismiss()
            }
        }
        pupUp.show(supportFragmentManager, "")
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