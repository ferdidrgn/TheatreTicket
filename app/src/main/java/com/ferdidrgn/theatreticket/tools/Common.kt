package com.ferdidrgn.theatreticket.tools

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.ferdidrgn.theatreticket.base.Err
import com.securepreferences.BuildConfig


fun log(string: String) {
    if (BuildConfig.DEBUG) {
        Handler(Looper.getMainLooper()).post {
            Log.d("Theatre Ticket", string)
        }
    }
}

fun checkIfTokenDeleted(error: Err?) {
    if (error?.code == 405)
        ClientPreferences.inst.token = ""
}

fun showToast(message: String, context: Context) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}