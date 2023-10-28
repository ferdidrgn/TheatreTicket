package com.ferdidrgn.theatreticket.tools

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.finishAffinity
import com.ferdidrgn.theatreticket.commonModels.dummyData.Show
import com.ferdidrgn.theatreticket.enums.ToMain
import com.ferdidrgn.theatreticket.ui.termsAndConditions.TermsAndConditionsActivity
import com.ferdidrgn.theatreticket.ui.language.LanguageActivity
import com.ferdidrgn.theatreticket.ui.main.MainActivity
import com.ferdidrgn.theatreticket.ui.onbarding.OnboardingActivity
import com.ferdidrgn.theatreticket.ui.showDetails.ShowDetailsActivity
import com.ferdidrgn.theatreticket.ui.showOperations.ShowOperationsActivity
import java.io.Serializable

class NavHandler {

    companion object {
        val instance = NavHandler()
    }

    fun toMainActivityFinishAffinity(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
        finishAffinity(context as AppCompatActivity)
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

    fun toLanguageActivity(context: Context) {
        val intent = Intent(context, LanguageActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun toTermsAndConditionsActivity(context: Context) {
        val intent = Intent(context, TermsAndConditionsActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun toShowDetailsActivity(context: Context, show: ArrayList<Show?>) {
        val intent = Intent(context, ShowDetailsActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra(SHOW, show as Serializable)
        context.startActivity(intent)
    }
}