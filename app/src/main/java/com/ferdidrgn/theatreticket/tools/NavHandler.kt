package com.ferdidrgn.theatreticket.tools

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.finishAffinity
import com.ferdidrgn.theatreticket.enums.ToMain
import com.ferdidrgn.theatreticket.ui.language.LanguageActivity
import com.ferdidrgn.theatreticket.ui.main.MainActivity
import com.ferdidrgn.theatreticket.ui.showAdd.ShowAddActivity
import com.ferdidrgn.theatreticket.ui.showOperations.ShowOperationsActivity

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

    fun toShowAddActivity(context: Context) {
        val intent = Intent(context, ShowAddActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun toShowOperationsActivity( context: Context) {
        val intent = Intent(context, ShowOperationsActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun toLanguageActivity(context: Context) {
        val intent = Intent(context, LanguageActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}