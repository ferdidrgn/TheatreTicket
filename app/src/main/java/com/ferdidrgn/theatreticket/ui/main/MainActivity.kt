package com.ferdidrgn.theatreticket.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseActivity
import com.ferdidrgn.theatreticket.databinding.ActivityMainBinding
import com.ferdidrgn.theatreticket.enums.ToMain
import com.ferdidrgn.theatreticket.tools.ClientPreferences
import com.ferdidrgn.theatreticket.tools.NavHandler
import com.ferdidrgn.theatreticket.tools.TO_MAIN
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    private lateinit var navController: NavController
    override fun getVM(): Lazy<MainViewModel> = viewModels()

    override fun getDataBinding(): ActivityMainBinding =
        ActivityMainBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstance: Bundle?) {

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNav =
            findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNav)
        bottomNav.setupWithNavController(navController)

        if (ClientPreferences.inst.isFirstLaunch == true)
            NavHandler.instance.toOnboardingActivity(this)
        else observe()
    }

    private fun observe() {
        intent.getSerializableExtra(TO_MAIN)?.let { data ->
            moveToWhere(data as ToMain)
        }
    }

    private fun moveToWhere(toWhere: ToMain) {
        binding.apply {
            when (toWhere) {
                ToMain.Home -> bottomNav.selectedItemId = R.id.homeFragmentNav

                ToMain.TicketBuy -> bottomNav.selectedItemId = R.id.ticketBuyFragmentNav

                ToMain.TicketSearch -> bottomNav.selectedItemId = R.id.ticketSearchFragmentNav

                ToMain.Settings -> bottomNav.selectedItemId = R.id.settingsFragmentNav
            }
        }
    }

    override fun changeTheme() = setTheme(R.style.Theme_TheatreTicket)

}