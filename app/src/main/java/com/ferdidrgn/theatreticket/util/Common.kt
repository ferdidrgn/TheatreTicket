package com.ferdidrgn.theatreticket.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.util.base.Err
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.securepreferences.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*


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

fun statusBarColor(context: Context, window: Window, color: Int = R.color.primary2) {
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

fun isEmailValid(email: String): Boolean {
    val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
    return emailRegex.matches(email)
}

fun getBitmap(imageUrl: String, context: Context, block: (Bitmap?) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        block(Glide.with(context).asBitmap().load(imageUrl).submit(50, 50).get())
    }
}

fun bitMapFromVector(context: Context, vectorResID: Int): BitmapDescriptor? {
    val vectorDrawable = ContextCompat.getDrawable(context, vectorResID)
    vectorDrawable?.setBounds(
        0,
        0,
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight
    )
    val bitmap =
        vectorDrawable?.intrinsicWidth?.let {
            Bitmap.createBitmap(
                it,
                vectorDrawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
        }
    val canvas = bitmap?.let { Canvas(it) }
    if (canvas != null) {
        vectorDrawable.draw(canvas)
    }
    return bitmap?.let { BitmapDescriptorFactory.fromBitmap(it) }
}

fun showToast(
    message: String,
    context: Context? = com.ferdidrgn.theatreticket.TheatreTicketApp.inst.applicationContext
) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun getContext(): Context {
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

fun calculateElapsed(dateString: String, whichData: String): String {

    val years: Int
    val months: Int
    val days: Int

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val givenDate = LocalDate.parse(dateString, dateFormatter)
        val currentDate = LocalDate.now()

        val period = Period.between(givenDate, currentDate)

        years = period.years
        months = period.months
        days = period.days
    } else {

        val format = SimpleDateFormat("dd-MM-yyyy", Locale.US)
        val givenDate: Date = format.parse(dateString) as Date
        val currentDate = Date()

        val difference = currentDate.time - givenDate.time
        val remainingDays = (difference / (1000 * 60 * 60 * 24)).toInt()

        years = remainingDays / 365
        months = (remainingDays % 365) / 30
        days = remainingDays % 30
    }

    return when (whichData) {
        "year" -> years.toString()
        "month" -> months.toString()
        "day" -> days.toString()
        else -> ""
    }
}