package com.ferdidrgn.theatreticket.ui.main.ticketBuy

import android.content.Context
import android.os.Bundle
import com.ferdidrgn.theatreticket.R
import androidx.fragment.app.viewModels
import com.ferdidrgn.theatreticket.base.BaseFragment
import com.ferdidrgn.theatreticket.base.BasePopUp
import com.ferdidrgn.theatreticket.databinding.FragmentTicketBuyBinding
import com.ferdidrgn.theatreticket.tools.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TicketBuyFragment : BaseFragment<TicketBuyViewModel, FragmentTicketBuyBinding>() {

    override fun getVM(): Lazy<TicketBuyViewModel> = viewModels()

    override fun getDataBinding(): FragmentTicketBuyBinding =
        FragmentTicketBuyBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstanceState: Bundle?) {
        binding.viewModel = viewModel

        viewModel.buyTicketPopUp.observe(viewLifecycleOwner) {
            buyTicketPopUp(requireContext())
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessagePopUp(requireContext(), errorMessage!!)
        }
    }


    private fun buyTicketPopUp(context: Context) {
        val pupUp = BasePopUp()
        pupUp.apply {
            setPositiveText(context.getString(R.string.yes))
            setNegativeText(context.getString(R.string.no))
            setTitle(context.getString(R.string.are_you_serious))
            setOnPositiveClick {
                viewModel.checkPhoneNumber() //phoneNumber = viewModel.phoneNumber.value
                dismiss()
            }
            setOnNegativeClick {
                dismiss()
            }
        }
        pupUp.show(parentFragmentManager, "")
    }

    private fun errorMessagePopUp(context: Context, errorMessage: String) {
        val pupUp = BasePopUp(isError = true)
        pupUp.apply {
            setPositiveText(context.getString(R.string.done))
            setDesc(errorMessage)
            setOnPositiveClick {
                dismiss()
            }
        }
        pupUp.show(parentFragmentManager, "")
    }
}