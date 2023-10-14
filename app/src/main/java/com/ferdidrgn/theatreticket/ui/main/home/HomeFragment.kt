package com.ferdidrgn.theatreticket.ui.main.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.viewModels
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseFragment
import com.ferdidrgn.theatreticket.base.BasePopUp
import com.ferdidrgn.theatreticket.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {
    override fun getVM(): Lazy<HomeViewModel> = viewModels()

    override fun getDataBinding(): FragmentHomeBinding =
        FragmentHomeBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstanceState: Bundle?) {

        observe()

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            messagePopUp(requireContext(), errorMessage!!, false)
        }
    }

    private fun observe() {
        viewModel.searchTicket()
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