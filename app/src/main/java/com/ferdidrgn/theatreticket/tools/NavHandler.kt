package com.ferdidrgn.theatreticket.tools

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.finishAffinity
import com.ferdidrgn.theatreticket.commonModels.dummyData.Show
import com.ferdidrgn.theatreticket.commonModels.dummyData.Stage
import com.ferdidrgn.theatreticket.enums.ToMain
import com.ferdidrgn.theatreticket.enums.WhichEditProfile
import com.ferdidrgn.theatreticket.enums.WhichTermsAndPrivace
import com.ferdidrgn.theatreticket.ui.buyTicket.TicketBuyActivity
import com.ferdidrgn.theatreticket.ui.editProfile.EditProfileActivity
import com.ferdidrgn.theatreticket.ui.termsAndConditionsAndPrivacePolicy.TermsAndConditionsandPrivacePolicyActivity
import com.ferdidrgn.theatreticket.ui.language.LanguageActivity
import com.ferdidrgn.theatreticket.ui.login.LoginActivity
import com.ferdidrgn.theatreticket.ui.main.MainActivity
import com.ferdidrgn.theatreticket.ui.onboarding.OnboardingActivity
import com.ferdidrgn.theatreticket.ui.otp.OTPActivity
import com.ferdidrgn.theatreticket.ui.showDetails.ShowDetailsActivity
import com.ferdidrgn.theatreticket.ui.showOperations.ShowOperationsActivity
import com.ferdidrgn.theatreticket.ui.stage.StageActivity
import com.ferdidrgn.theatreticket.ui.stageOperations.StageOperationsActivity
import java.io.Serializable

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
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(TO_MAIN, ToMain.Settings)
        context.startActivity(intent)
    }

    fun toMainActivityFinishAffinity(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
        finishAffinity(context as AppCompatActivity)
    }

    fun toBuyTicketActivity(context: Context, show: Show?, stage: Stage?) {
        val intent = Intent(context, TicketBuyActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra(SHOW, show)
        intent.putExtra(STAGE, stage)
        context.startActivity(intent)
    }

    fun toMainActivity(context: Context, toMain: ToMain) {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra(TO_MAIN, toMain)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
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

    fun toLoginActivity(context: Context) {
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun toLoginActivity(context: Context, isSettingsClickedLogIn: Boolean) {
        val intent = Intent(context, LoginActivity::class.java)
        intent.putExtra(IS_SETTINGS_CLICKED_LOG_IN, isSettingsClickedLogIn)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun toLanguageActivity(context: Context) {
        val intent = Intent(context, LanguageActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun toTermsConditionsAndPrivacePolicyActivity(
        context: Context,
        whichTermsAndPrivace: WhichTermsAndPrivace
    ) {
        val intent = Intent(context, TermsAndConditionsandPrivacePolicyActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra(WHICH_TERMS_PRIVACE, whichTermsAndPrivace)
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