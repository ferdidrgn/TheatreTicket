package com.ferdidrgn.theatreticket.ui.showOperations

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.viewModels
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseActivity
import com.ferdidrgn.theatreticket.base.BasePopUp
import com.ferdidrgn.theatreticket.databinding.ActivityShowOperationsBinding
import com.ferdidrgn.theatreticket.tools.NavHandler
import com.ferdidrgn.theatreticket.tools.onClickDelayed
import com.tayfuncesur.curvedbottomsheet.CurvedBottomSheet
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ShowOperationsActivity :
    BaseActivity<ShowOperationsViewModel, ActivityShowOperationsBinding>() {

    override fun getVM(): Lazy<ShowOperationsViewModel> = viewModels()

    override fun getDataBinding(): ActivityShowOperationsBinding =
        ActivityShowOperationsBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstance: Bundle?) {
        binding.viewModel = viewModel
        binding.showOperationsAdapter = ShowOperationsAdapter(viewModel)
        observe()
        //binding.customToolbar.backIconOnBackPress(this)


        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        CurvedBottomSheet(
            (displayMetrics.widthPixels / 6).toFloat(),
            view = binding.bottomSheet
        ).init()

    }

    private fun observe() {
        viewModel.getAllShow()

        binding.customToolbar.backIcon.onClickDelayed {
            onBackPressed()
        }

        binding.imgClose.onClickDelayed {
            viewModel.bottomSheetVisibility.postValue(false)
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage != null)
                messagePopUp(this, errorMessage, false)
        }

        viewModel.successMessage.observe(this) { successMessage ->
            if (successMessage != null)
                messagePopUp(this, successMessage, true)
        }

        viewModel.btnAddShowClicked.observe(this) {
            NavHandler.instance.toShowAddActivity(this)
        }

        viewModel.updatePopUp.observe(this) {
            popUp(this, true)
        }

        viewModel.deletePopUp.observe(this) {
            popUp(this, false)
        }

        viewModel.updateShowPopUp.observe(this) {
            updateShowPopUp(this)
        }
    }

    private fun popUp(context: Context, isUpdate: Boolean) {
        val pupUp = BasePopUp()
        pupUp.apply {
            setPositiveText(context.getString(R.string.yes))
            setNegativeText(context.getString(R.string.no))
            if (isUpdate) {
                setDesc(context.getString(R.string.are_you_serious_update))
                setOnPositiveClick {
                    viewModel.bottomSheetVisibility.postValue(true)
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

    private fun updateShowPopUp(context: Context) {
        val pupUp = BasePopUp()
        pupUp.apply {
            setPositiveText(context.getString(R.string.yes))
            setNegativeText(context.getString(R.string.no))
            setDesc(context.getString(R.string.are_you_serious))
            setOnPositiveClick {
                viewModel.updateShow()
                dismiss()
            }
            setOnNegativeClick {
                dismiss()
            }
        }
        pupUp.show(supportFragmentManager, "")
    }
}