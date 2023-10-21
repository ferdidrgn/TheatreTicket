package com.ferdidrgn.theatreticket.ui.main.settings

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseFragment
import com.ferdidrgn.theatreticket.base.BasePopUp
import com.ferdidrgn.theatreticket.databinding.FragmentSettingsBinding
import com.ferdidrgn.theatreticket.enums.ToMain
import com.ferdidrgn.theatreticket.tools.ClientPreferences
import com.ferdidrgn.theatreticket.tools.onClickDelayed
import com.ferdidrgn.theatreticket.tools.NavHandler
import com.ferdidrgn.theatreticket.tools.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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

    private fun clickListener() {
        binding.apply {
            btnSellTicket.onClickDelayed {
                NavHandler.instance.toMainActivity(requireContext(), ToMain.TicketBuy)
            }
            btnAddShow.onClickDelayed {
                NavHandler.instance.toShowAddActivity(requireContext())
            }
            btnShowDelete.onClickDelayed {
                NavHandler.instance.toShowDeleteActivity(requireContext())
            }
            btnLanguage.onClickDelayed {
                NavHandler.instance.toLanguageActivity(requireContext())
            }
            btnLogout.onClickDelayed {
                showLogoutDialog(requireContext())
            }
            binding.btnInviteFriends.setOnClickListener {
                //NavHandler.instance.inviteFriendsScreen(requireContext())
            }
            binding.btnDeleteAcc.setOnClickListener {
                //deleteAccountPopup()
            }
            btnContactUs.setOnClickListener {
                val i = Intent(Intent.ACTION_SEND)
                i.type = "message/rfc822"
                i.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.contact_email)))
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.contact_us_producter))
                i.putExtra(Intent.EXTRA_TEXT, getString(R.string.notification))
                try {
                    startActivity(Intent.createChooser(i, getString(R.string.send_mail)))
                } catch (ex: ActivityNotFoundException) {
                    showToast(getString(R.string.error_not_mail_installed))
                }
            }
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