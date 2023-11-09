package com.ferdidrgn.theatreticket.ui.main.ticketBuy

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import com.ferdidrgn.theatreticket.R
import androidx.fragment.app.viewModels
import com.ferdidrgn.theatreticket.base.BaseFragment
import com.ferdidrgn.theatreticket.base.BasePopUp
import com.ferdidrgn.theatreticket.databinding.FragmentTicketBuyBinding
import com.ferdidrgn.theatreticket.tools.builderADS
import com.ferdidrgn.theatreticket.ui.main.MainActivity
import com.tayfuncesur.curvedbottomsheet.CurvedBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TicketBuyFragment : BaseFragment<TicketBuyViewModel, FragmentTicketBuyBinding>() {

    override fun getVM(): Lazy<TicketBuyViewModel> = viewModels()

    override fun getDataBinding(): FragmentTicketBuyBinding =
        FragmentTicketBuyBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        builderADS(requireContext(), binding.adView)
        binding.cdpDate.setCustomDataPickerClick(requireContext()) { date ->
            viewModel.age.value = date
        }
        bottomSheetInit()
        getGridLayout()
        observe()

    }

    private fun observe() {

        viewModel.buyTicketPopUp.observe(viewLifecycleOwner) {
            buyTicketPopUp(requireContext())
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

    private fun bottomSheetInit() {
        val displayMetrics = DisplayMetrics()
        (requireActivity() as MainActivity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        CurvedBottomSheet(
            (displayMetrics.widthPixels / 6).toFloat(),
            view = binding.bottomSheet
        ).init()
    }

    fun getGridLayout() {
        viewModel.seat.observe(this) { seatList ->
            viewModel.seatColumnCount.observe(this) { columnCount ->
                binding.customSeatPlan.setUpGridLayoutManager(seatList, columnCount)
            }
        }
    }

    private fun buyTicketPopUp(context: Context) {
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