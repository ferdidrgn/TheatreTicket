package com.ferdidrgn.theatreticket.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.enums.ToMain
import com.ferdidrgn.theatreticket.tools.TO_MAIN

class MainActivity : AppCompatActivity() {
    //NOT: THEN WE NEED TO ADD DAGGER HILT AND BASE ACTIVITY
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNav = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNav)
        bottomNav.setupWithNavController(navController)

        intent.getSerializableExtra(TO_MAIN)?.let { data->
            moveToWhere(data as ToMain)
        }
    }
    private fun moveToWhere(toWhere: ToMain) {
        val bottomNav = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNav)
        when (toWhere) {
            ToMain.Home -> bottomNav.selectedItemId = R.id.homeFragmentNav

            ToMain.TicketBuy -> bottomNav.selectedItemId = R.id.ticketBuyFragmentNav

            ToMain.TicketSearch -> bottomNav.selectedItemId = R.id.ticketSearchFragmentNav

            ToMain.Settings -> bottomNav.selectedItemId = R.id.settingsFragmentNav

            else -> bottomNav.selectedItemId = R.id.homeFragmentNav
        }
    }

}