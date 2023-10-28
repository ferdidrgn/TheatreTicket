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
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
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

fun builderADS(context: Context, view: AdView) {
    MobileAds.initialize(context)
    val adRequest = AdRequest.Builder().build()
    view.loadAd(adRequest)
    view.adListener = object : AdListener() {
        override fun onAdClicked() {
            // Code to be executed when the user clicks on an ad.
        }

        override fun onAdClosed() {
            // Code to be executed when the user is about to return
            // to the app after tapping on an ad.
        }

        override fun onAdFailedToLoad(adError: LoadAdError) {
            // Code to be executed when an ad request fails.
        }

        override fun onAdImpression() {
            // Code to be executed when an impression is recorded
            // for an ad.
        }

        override fun onAdLoaded() {
            // Code to be executed when an ad finishes loading.
        }

        override fun onAdOpened() {
            // Code to be executed when an ad opens an overlay that
            // covers the screen.
        }
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

fun getMonthFormat(context: Context, month: Int): String {
    val stringId = when (month) {
        1 -> R.string.jan
        2 -> R.string.feb
        3 -> R.string.mar
        4 -> R.string.apr
        5 -> R.string.may
        6 -> R.string.jun
        7 -> R.string.jul
        8 -> R.string.aug
        9 -> R.string.sep
        10 -> R.string.oct
        11 -> R.string.nov
        12 -> R.string.dec
        else -> R.string.jan
    }
    return context.resources.getString(stringId)
}