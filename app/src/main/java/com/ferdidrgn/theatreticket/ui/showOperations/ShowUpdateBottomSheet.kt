package com.ferdidrgn.theatreticket.ui.showOperations

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.viewModels
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseBottomSheet
import com.ferdidrgn.theatreticket.base.BasePopUp
import com.ferdidrgn.theatreticket.databinding.FragmentShowUpdateBottomSheetBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowUpdateBottomSheet(private val onRefresh: (() -> Unit)?) :
    BaseBottomSheet<ShowOperationsViewModel, FragmentShowUpdateBottomSheetBinding>() {

    override fun getVM(): Lazy<ShowOperationsViewModel> = viewModels()

    override fun getDataBinding(): FragmentShowUpdateBottomSheetBinding =
        FragmentShowUpdateBottomSheetBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstance: Bundle?) {
        binding.viewModel = viewModel
        observe()
    }

    fun observe() {
        viewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage != null)
                messagePopUp(requireContext(), errorMessage, false)
        }
        viewModel.successMessage.observe(this) { successMessage ->
            if (successMessage != null)
                messagePopUp(requireContext(), successMessage, true)
        }

        viewModel.updateShowPopUp.observe(this) {
            updateShowPopUp(requireContext())
        }

        viewModel.updateSuccess.observe(this) {
            onRefresh?.let { it() }
            dismiss()
        }
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
        pupUp.show(parentFragmentManager, "")
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
        pupUp.show(parentFragmentManager, "")
    }

}