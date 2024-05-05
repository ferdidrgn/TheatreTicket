package com.ferdidrgn.theatreticket.presentation.ui.main.ticketSearch

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.viewModels
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseFragment
import com.ferdidrgn.theatreticket.base.BasePopUp
import com.ferdidrgn.theatreticket.databinding.FragmentTicketSearchBinding
import com.ferdidrgn.theatreticket.tools.builderADS
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TicketSearchFragment : BaseFragment<TicketSearchViewModel, FragmentTicketSearchBinding>() {
    override fun getVM(): Lazy<TicketSearchViewModel> = viewModels()

    override fun getDataBinding(): FragmentTicketSearchBinding =
        FragmentTicketSearchBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        binding.findTicketAdapter = FindTicketAdapter(viewModel)

        builderADS(requireContext(), binding.adView)

        observe()
    }

    private fun observe() {
        viewModel.searchTicketPopUp.observe(viewLifecycleOwner) {
            searchTicketPopUp(requireContext())
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null)
                messagePopUp(requireContext(), errorMessage, false)
        }
        viewModel.successMessage.observe(viewLifecycleOwner) { successMessage ->
            if (successMessage != null)
                messagePopUp(requireContext(), successMessage, true)
        }
    }

    private fun searchTicketPopUp(context: Context) {
        val pupUp = BasePopUp()
        pupUp.apply {
            setPositiveText(context.getString(R.string.yes))
            setNegativeText(context.getString(R.string.no))
            setDesc(context.getString(R.string.are_you_serious))
            setOnPositiveClick {
                viewModel.checkRequestFields()
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