package com.ferdidrgn.theatreticket.tools

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.Err
import com.securepreferences.BuildConfig


fun log(string: String) {
    if (BuildConfig.DEBUG) {
        Handler(Looper.getMainLooper()).post {
            Log.d("Theatre Ticket", string)
        }
    }
}

fun handleStatusBar(window: Window) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }
}

fun statusBarColor(context: Context, window: Window, color: Int = R.color.main_dark) {
    if (Build.VERSION.SDK_INT >= 21) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = context.resources.getColor(color)
        window.navigationBarColor = context.resources.getColor(color)
    }
}

fun checkIfTokenDeleted(error: Err?) {
    if (error?.code == 405)
        ClientPreferences.inst.token = ""
}

fun showToast(
    message: String,
    context: Context? = com.ferdidrgn.theatreticket.TheatreTicketApp.inst.applicationContext
) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun getContext(): Context? {
    return com.ferdidrgn.theatreticket.TheatreTicketApp.inst.applicationContext
}