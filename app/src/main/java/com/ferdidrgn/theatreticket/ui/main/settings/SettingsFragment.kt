package com.ferdidrgn.theatreticket.ui.main.settings

import android.content.Context
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseFragment
import com.ferdidrgn.theatreticket.base.BasePopUp
import com.ferdidrgn.theatreticket.databinding.FragmentSettingsBinding
import com.ferdidrgn.theatreticket.enums.ToMain
import com.ferdidrgn.theatreticket.tools.ADMIN_BUY_TICKET
import com.ferdidrgn.theatreticket.tools.ClientPreferences
import com.ferdidrgn.theatreticket.tools.onClickDelayed
import com.ferdidrgn.theatreticket.tools.showToast
import com.ferdidrgn.theatreticket.tools.NavHandler
import com.ferdidrgn.theatreticket.ui.main.home.HomeFragment

class SettingsFragment : BaseFragment<SettingsViewModel, FragmentSettingsBinding>() {
    override fun getVM(): Lazy<SettingsViewModel> = viewModels()

    override fun getDataBinding(): FragmentSettingsBinding =
        FragmentSettingsBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstanceState: Bundle?) {

        binding.viewModel = viewModel
        viewModel.selectedLayout()
        clickListener()

    }

    override fun onResume() {
        super.onResume()
        viewModel.getMyUser()
    }

    fun clickListener() {
        binding.btnSellTicket.onClickDelayed {
            NavHandler.instance.toMainActivity(requireContext(), ToMain.TicketBuy)
        }
        binding.btnLogout.onClickDelayed {
            showLogoutDialog(requireContext())
        }
    }

    private fun showLogoutDialog(context: Context) {
        val pupUp = BasePopUp()
        pupUp.apply {
            setPositiveText(context.getString(R.string.yes))
            setNegativeText(context.getString(R.string.no))
            setTitle(context.getString(R.string.log_out))
            setDesc(context.getString(R.string.are_you_sure_you_want_to_logout))
            setOnPositiveClick {
                ClientPreferences.inst.token = ""
                ClientPreferences.inst.userID = ""
                ClientPreferences.inst.userPhone = ""
                ClientPreferences.inst.userFirstName = ""
                ClientPreferences.inst.userLastName = ""
                NavHandler.instance.toMainActivity(requireContext(), ToMain.Home)
            }
            setOnNegativeClick {
                dismiss()
            }
        }

        pupUp.show(parentFragmentManager, "")
    }

}