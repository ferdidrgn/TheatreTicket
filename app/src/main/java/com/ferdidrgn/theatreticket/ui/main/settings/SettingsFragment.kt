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
import com.ferdidrgn.theatreticket.enums.WhichEditProfile
import com.ferdidrgn.theatreticket.enums.WhichTermsAndPrivace
import com.ferdidrgn.theatreticket.tools.*
import com.ferdidrgn.theatreticket.ui.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
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
        clickEvents()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getMyUser()
    }

    private fun clickEvents() {

        viewModel.btnEditProfileClicked.observe(viewLifecycleOwner) {
            NavHandler.instance.toEditProfileActivity(requireContext(), WhichEditProfile.Settings)
        }
        viewModel.btnSellTicketClicked.observe(viewLifecycleOwner) {
            NavHandler.instance.toMainActivity(requireContext(), ToMain.TicketBuy)
        }
        viewModel.btnShowOperationsClicked.observe(viewLifecycleOwner) {
            NavHandler.instance.toShowOperationsActivity(requireContext())
        }
        viewModel.btnStageOperationsClicked.observe(viewLifecycleOwner) {
            NavHandler.instance.toStageOperationsActivity(requireContext())
        }
        viewModel.btnLanguageClicked.observe(viewLifecycleOwner) {
            NavHandler.instance.toLanguageActivity(requireContext())
        }
        viewModel.btnOnShareAppClick.observe(viewLifecycleOwner) {
            viewModel.checkRole()
        }
        viewModel.btnRateAppClicked.observe(viewLifecycleOwner) {
            reviewRequest()
        }
        viewModel.btnContactUsClicked.observe(viewLifecycleOwner) {
            contactUs()
        }
        viewModel.btnChangeThemeClicked.observe(viewLifecycleOwner) {
            themeLightOrDark()
        }
        viewModel.btnLogoutClicked.observe(viewLifecycleOwner) {
            showLogoutDialog(requireContext())
        }
        viewModel.btnLogInClicked.observe(viewLifecycleOwner) {
            goToLoginAndFinishAffinity(requireActivity() as MainActivity)
        }
        viewModel.btnPrivacePolicyClicked.observe(viewLifecycleOwner) {
            NavHandler.instance.toTermsConditionsAndPrivacePolicyActivity(
                requireContext(),
                WhichTermsAndPrivace.PrivaceAndPolicy
            )
        }
        viewModel.btnTermsAndConditionsClicked.observe(viewLifecycleOwner) {
            NavHandler.instance.toTermsConditionsAndPrivacePolicyActivity(
                requireContext(),
                WhichTermsAndPrivace.TermsAndCondtion
            )
        }
    }

    private fun reviewRequest() {
        try {
            val manager = ReviewManagerFactory.create(requireActivity())
            manager.requestReviewFlow()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val reviewInfo: ReviewInfo = task.result
                        manager.launchReviewFlow(requireActivity(), reviewInfo)
                            .addOnFailureListener {
                                viewModel.errorMessage.postValue(it.message)
                            }.addOnCompleteListener {
                                viewModel.successMessage.postValue(getString(R.string.thank_you_for_review))
                            }
                    }
                }.addOnFailureListener {
                    viewModel.errorMessage.postValue(it.message)
                }
        } catch (e: ActivityNotFoundException) {
            viewModel.errorMessage.postValue(e.localizedMessage)
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
                            goToLoginAndFinishAffinity(requireActivity() as MainActivity)

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
            goToLoginAndFinishAffinity(requireActivity() as MainActivity)
        }
    }

    private fun themeLightOrDark() {

        if (ClientPreferences.inst.isDarkMode == true) {
            ClientPreferences.inst.isDarkMode = false
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            NavHandler.instance.toChangeTheme(requireActivity() as MainActivity)
            (requireActivity() as MainActivity).finish()
        } else {
            ClientPreferences.inst.isDarkMode = true
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            NavHandler.instance.toChangeTheme(requireActivity() as MainActivity)
            (requireActivity() as MainActivity).finish()
        }
    }

    private fun contactUs() {
        val eMail = viewModel.getContactUs()
        val i = Intent(Intent.ACTION_SEND)
        i.type = "message/rfc822"
        i.putExtra(Intent.EXTRA_EMAIL, arrayOf(eMail))
        i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.contact_us_producter))
        i.putExtra(Intent.EXTRA_TEXT, getString(R.string.notification))
        try {
            startActivity(Intent.createChooser(i, getString(R.string.send_mail)))
        } catch (ex: ActivityNotFoundException) {
            showToast(getString(R.string.error_not_mail_installed))
        }
    }

    private fun goToLoginAndFinishAffinity(context: MainActivity) {
        NavHandler.instance.toLoginActivity(requireContext(), true)
        context.finishAffinity()
    }
}