package com.ferdidrgn.theatreticket.ui.main.settings

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseFragment
import com.ferdidrgn.theatreticket.base.BasePopUp
import com.ferdidrgn.theatreticket.databinding.FragmentSettingsBinding
import com.ferdidrgn.theatreticket.enums.ToMain
import com.ferdidrgn.theatreticket.tools.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<SettingsViewModel, FragmentSettingsBinding>() {

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun getVM(): Lazy<SettingsViewModel> = viewModels()

    override fun getDataBinding(): FragmentSettingsBinding =
        FragmentSettingsBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstanceState: Bundle?) {

        binding.viewModel = viewModel
        viewModel.selectedLayout()
        builderADS(requireContext(), binding.adView)
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
            btnShowOperations.onClickDelayed {
                NavHandler.instance.toShowOperationsActivity(requireContext())
            }
            btnStageOperations.onClickDelayed {
            }
            btnLanguage.onClickDelayed {
                NavHandler.instance.toLanguageActivity(requireContext())
            }
            btnLogout.onClickDelayed {
                showLogoutDialog(requireContext())
            }
            binding.btnInviteFriends.onClickDelayed {
                //NavHandler.instance.inviteFriendsScreen(requireContext())
            }
            binding.btnDeleteAcc.onClickDelayed {
                //deleteAccountPopup()
            }
            btnContactUs.onClickDelayed {
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
            btnChangeTheme.onClickDelayed {
                themeLightOrDark()
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

                FirebaseMessaging.getInstance().deleteToken()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            if (ClientPreferences.inst.isGoogleSignIn == true)
                                signOutAndStartSignInActivity()

                            FirebaseAuth.getInstance().signOut()

                            viewModel.clearClientPreferences()
                            showToast(context.getString(R.string.success))
                            dismiss()
                        }
                    }
            }
            setOnNegativeClick {
                dismiss()
            }
        }

        pupUp.show(parentFragmentManager, "")
    }

    private fun signOutAndStartSignInActivity() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        mGoogleSignInClient.signOut().addOnCompleteListener(requireActivity()) {
            NavHandler.instance.toLoginActivity(requireContext())
        }
    }

    private fun themeLightOrDark() {

        if (ClientPreferences.inst.isDarkMode == true) {
            ClientPreferences.inst.isDarkMode = false
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            NavHandler.instance.toMainActivity(requireContext(), ToMain.Settings)
        } else {
            ClientPreferences.inst.isDarkMode = true
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            NavHandler.instance.toMainActivity(requireContext(), ToMain.Settings)
        }
    }
}