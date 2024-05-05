package com.ferdidrgn.theatreticket.util

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.finishAffinity
import com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData.Show
import com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData.Stage
import com.ferdidrgn.theatreticket.presentation.buyTicket.TicketBuyActivity
import com.ferdidrgn.theatreticket.presentation.editProfile.EditProfileActivity
import com.ferdidrgn.theatreticket.presentation.termsAndConditionsAndPrivacyPolicy.TermsAndConditionsAndPrivacyPolicyActivity
import com.ferdidrgn.theatreticket.presentation.language.LanguageActivity
import com.ferdidrgn.theatreticket.presentation.login.LoginActivity
import com.ferdidrgn.theatreticket.presentation.main.MainActivity
import com.ferdidrgn.theatreticket.presentation.onboarding.OnboardingActivity
import com.ferdidrgn.theatreticket.presentation.otp.OTPActivity
import com.ferdidrgn.theatreticket.presentation.showDetails.ShowDetailsActivity
import com.ferdidrgn.theatreticket.presentation.showOperations.ShowOperationsActivity
import com.ferdidrgn.theatreticket.presentation.stage.StageActivity
import com.ferdidrgn.theatreticket.presentation.stageOperations.StageOperationsActivity

class NavHandler {

    companion object {
        val instance = NavHandler()
    }

    fun toRestartApp(context: Context) {
        val packageManager = context.applicationContext.packageManager
        val intent =
            packageManager.getLaunchIntentForPackage(context.applicationContext.packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun toChangeTheme(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(TO_MAIN, ToMain.Settings)
        context.startActivity(intent)
    }

    fun toMainActivity(
        context: Context,
        toMain: ToMain = ToMain.Home,
        finishAffinity: Boolean = false
    ) {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra(TO_MAIN, toMain)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
        if (finishAffinity)
            finishAffinity(context as AppCompatActivity)
    }

    fun toBuyTicketActivity(context: Context, show: Show?, stage: Stage?) {
        val intent = Intent(context, TicketBuyActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra(SHOW, show)
        intent.putExtra(STAGE, stage)
        context.startActivity(intent)
    }

    fun toOnboardingActivity(context: Context) {
        val intent = Intent(context, OnboardingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun toShowOperationsActivity(context: Context) {
        val intent = Intent(context, ShowOperationsActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun toLoginActivity(context: Context, isSettingsClickedLogIn: Boolean = false) {
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK

        if (isSettingsClickedLogIn)
            intent.putExtra(IS_SETTINGS_CLICKED_LOG_IN, isSettingsClickedLogIn)

        context.startActivity(intent)
    }

    fun toLanguageActivity(context: Context) {
        val intent = Intent(context, LanguageActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun toTermsConditionsAndPrivacyPolicyActivity(
        context: Context,
        whichTermsAndPrivacy: WhichTermsAndPrivacy
    ) {
        val intent = Intent(context, TermsAndConditionsAndPrivacyPolicyActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra(WHICH_TERMS_PRIVACY, whichTermsAndPrivacy)
        context.startActivity(intent)
    }

    fun toShowDetailsActivity(context: Context, show: Show) {
        val intent = Intent(context, ShowDetailsActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra(SHOW, show)
        context.startActivity(intent)
    }

    fun toShowDetailsIDActivity(context: Context, showID: String) {
        val intent = Intent(context, ShowDetailsActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra(SHOW_ID, showID)
        context.startActivity(intent)
    }

    fun toEditProfileActivity(
        context: Context, whichEditProfile: WhichEditProfile
    ) {
        val intent = Intent(context, EditProfileActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra(WHICH_EDIT_PROFILE, whichEditProfile)
        context.startActivity(intent)
    }

    fun toStageOperationsActivity(context: Context) {
        val intent = Intent(context, StageOperationsActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun toStageActivity(context: Context, stage: Stage?) {
        val intent = Intent(context, StageActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra(STAGE, stage)
        context.startActivity(intent)
    }

    fun toOTPActivity(context: Context) {
        val intent = Intent(context, OTPActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}