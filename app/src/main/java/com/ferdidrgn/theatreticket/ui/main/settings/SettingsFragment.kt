package com.ferdidrgn.theatreticket.ui.main.settings

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseFragment
import com.ferdidrgn.theatreticket.databinding.FragmentSettingsBinding
import com.ferdidrgn.theatreticket.tools.ADMIN_BUY_TICKET
import com.ferdidrgn.theatreticket.tools.onClickDelayed
import com.ferdidrgn.theatreticket.tools.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<SettingsViewModel, FragmentSettingsBinding>() {
    override fun getVM(): Lazy<SettingsViewModel> = viewModels()

    override fun getDataBinding(): FragmentSettingsBinding =
        FragmentSettingsBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstanceState: Bundle?) {
        showToast("SettingsFragment", requireContext())

        binding.btnSellTicket.onClickDelayed {
            val bundle = bundleOf(ADMIN_BUY_TICKET to true)
            findNavController().navigate(R.id.action_navigation_settings_fragment_to_navigation_ticket_buy, bundle)
        }
    }

}