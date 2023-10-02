package com.ferdidrgn.theatreticket.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseActivity
import com.ferdidrgn.theatreticket.databinding.ActivityMainBinding
import com.ferdidrgn.theatreticket.databinding.FragmentSettingsBinding
import com.ferdidrgn.theatreticket.enums.ToMain
import com.ferdidrgn.theatreticket.tools.TO_MAIN
import com.ferdidrgn.theatreticket.ui.main.settings.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    private lateinit var navController: NavController
    override fun getVM(): Lazy<MainViewModel> = viewModels()

    override fun getDataBinding(): ActivityMainBinding =
        ActivityMainBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstanceState: Bundle?) {

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNav =
            findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNav)
        bottomNav.setupWithNavController(navController)

        intent.getSerializableExtra(TO_MAIN)?.let { data ->
            moveToWhere(data as ToMain)
        }
    }

    private fun moveToWhere(toWhere: ToMain) {
        val bottomNav =
            findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNav)
        when (toWhere) {
            ToMain.Home -> bottomNav.selectedItemId = R.id.homeFragmentNav

            ToMain.TicketBuy -> bottomNav.selectedItemId = R.id.ticketBuyFragmentNav

            ToMain.TicketSearch -> bottomNav.selectedItemId = R.id.ticketSearchFragmentNav

            ToMain.Settings -> bottomNav.selectedItemId = R.id.settingsFragmentNav

            else -> bottomNav.selectedItemId = R.id.homeFragmentNav
        }
    }

}