package com.ferdidrgn.theatreticket

import android.app.Application
import com.ferdidrgn.theatreticket.util.ClientPreferences
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TheatreTicketApp : Application() {

    companion object {
        var inst = TheatreTicketApp()
    }

    override fun onCreate() {
        super.onCreate()
        inst = this
        ClientPreferences.inst = ClientPreferences()
        FirebaseApp.initializeApp(this)
    }
}