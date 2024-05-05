package com.ferdidrgn.theatreticket.presentation.main.settings

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.SkuDetailsParams
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.util.base.BaseFragment
import com.ferdidrgn.theatreticket.util.base.BasePopUp
import com.ferdidrgn.theatreticket.databinding.FragmentSettingsBinding
import com.ferdidrgn.theatreticket.presentation.main.MainActivity
import com.ferdidrgn.theatreticket.util.APP_LINK
import com.ferdidrgn.theatreticket.util.ClientPreferences
import com.ferdidrgn.theatreticket.util.DONATION_SMALL
import com.ferdidrgn.theatreticket.util.NavHandler
import com.ferdidrgn.theatreticket.util.ToMain
import com.ferdidrgn.theatreticket.util.WhichEditProfile
import com.ferdidrgn.theatreticket.util.WhichTermsAndPrivacy
import com.ferdidrgn.theatreticket.util.showToast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<SettingsViewModel, FragmentSettingsBinding>(),
    PurchasesUpdatedListener {

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var billingClient: BillingClient
    private var isBillingLoadSuccess = false

    override fun getVM(): Lazy<SettingsViewModel> = viewModels()

    override fun getDataBinding(): FragmentSettingsBinding =
        FragmentSettingsBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstanceState: Bundle?) {

        binding.viewModel = viewModel
        binding.visibility = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
        viewModel.selectedLayout()
        setAds(binding.adView)
        initBillingClint()
        clickEvents()
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
        viewModel.btnNotificationPermission.observe(viewLifecycleOwner) {
            openNotificationSettings()
        }
        viewModel.btnOnShareAppClick.observe(viewLifecycleOwner) {
            requireContext().shareLink()
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
        viewModel.btnPrivacyPolicyClicked.observe(viewLifecycleOwner) {
            goToTermsAndConditionsAction(true)
        }
        viewModel.btnTermsAndConditionsClicked.observe(viewLifecycleOwner) {
            goToTermsAndConditionsAction(false)
        }
        viewModel.btnBuyCoffeeGooglePlayClick.observe(viewLifecycleOwner) {
            if (isBillingLoadSuccess)
                loadAllSkus(DONATION_SMALL)
            else
                showToast(getString(R.string.billing_error))
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getMyUser()
        setAds(binding.adView)
    }

    private fun Context.shareLink() {
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, APP_LINK)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun goToTermsAndConditionsAction(isPrivacyPolicy: Boolean) {
        NavHandler.instance.toTermsConditionsAndPrivacyPolicyActivity(
            requireContext(),
            if (isPrivacyPolicy) WhichTermsAndPrivacy.PrivacyAndPolicy else WhichTermsAndPrivacy.TermsAndCondition
        )
    }

    private fun openNotificationSettings() {
        val intent = Intent()
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, requireActivity().packageName)
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                intent.putExtra("app_package", requireActivity().packageName)
                intent.putExtra("app_uid", requireActivity().applicationInfo.uid)
            }

            else -> {
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                intent.data = android.net.Uri.parse("package:${requireActivity().packageName}")
            }
        }
        requireContext().startActivity(intent)
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
            .requestIdToken("1"/*getString(R.string.default_web_client_id)*/)
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

    private fun initBillingClint() {
        billingClient = BillingClient.newBuilder(requireContext())
            .setListener(this)
            .enablePendingPurchases()
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                // Faturalandırma hizmeti bağlantısı kesildi, gerekirse tekrar bağlanmayı deneyebiliriz.
                showToast(getString(R.string.billing_error))
            }

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK)
                    isBillingLoadSuccess =
                        true // Faturalandırma bağlantısı başarıyla kuruldu, satın alma işlemlerini gerçekleştirebiliriz.
                else
                    showToast(getString(R.string.billing_error) + " " + billingResult.debugMessage) //"Faturalandırma bağlantısı başarısız: ${billingResult.debugMessage}"
            }
        })
    }

    private fun loadAllSkus(productId: String) {
        val skuDetailsParams = SkuDetailsParams.newBuilder()
            .setSkusList(listOf(productId))
            .setType(BillingClient.SkuType.INAPP)
            .build()

        billingClient.querySkuDetailsAsync(skuDetailsParams) { billingResult, skuDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                skuDetailsList?.let { skuDetails ->
                    if (skuDetails.isNotEmpty()) {
                        val skuDetails = skuDetails[0]
                        val billingFlowParams = BillingFlowParams.newBuilder()
                            .setSkuDetails(skuDetails)
                            .build()

                        billingClient.launchBillingFlow(requireActivity(), billingFlowParams)
                    }
                }
            } else
                showToast(getString(R.string.billing_details_failed) + " " + billingResult.debugMessage)
        }
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null)
            handlePurchases(purchases)
        else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED)
            showToast(getString(R.string.billing_cancel))   //"Kullanıcı satın alma işlemi iptal etti"
        else
            showToast(getString(R.string.billing_failed) + " " + billingResult.debugMessage)
    }

    private fun handlePurchases(purchases: List<Purchase>) {
        for (purchase in purchases) {
            if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED)
                consumePurchase(purchase) // Satın alınan ürünleri işleme almak için burada gerekli işlemler yapılabilir.
        }
    }

    private fun consumePurchase(purchase: Purchase) {
        val consumeParams = ConsumeParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()

        billingClient.consumeAsync(consumeParams) { billingResult, purchaseToken ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchaseToken != null)
                showToast(getString(R.string.billing_consume_success)) // Satın alınan ürün başarıyla tüketildi, gerekli işlemler yapılabilir.
            else
                showToast(getString(R.string.billing_consume_failed) + " " + billingResult.debugMessage)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        billingClient.endConnection()
    }
}