package com.ferdidrgn.theatreticket.tools

import android.content.Context
import android.content.Intent
import com.ferdidrgn.theatreticket.enums.ToMain
import com.ferdidrgn.theatreticket.ui.main.MainActivity

class NavHandler {

    companion object {
        val instance = NavHandler()
    }

    fun toMainActivity(context: Context, toMain: ToMain) {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra(TO_MAIN, toMain)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}