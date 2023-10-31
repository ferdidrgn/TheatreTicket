package com.ferdidrgn.theatreticket.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseActivity
import com.ferdidrgn.theatreticket.databinding.ActivityMainBinding
import com.ferdidrgn.theatreticket.enums.ToMain
import com.ferdidrgn.theatreticket.repository.UserFirebaseQueries
import com.ferdidrgn.theatreticket.tools.ClientPreferences
import com.ferdidrgn.theatreticket.tools.NavHandler
import com.ferdidrgn.theatreticket.tools.TO_MAIN
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    private lateinit var navController: NavController
    override fun getVM(): Lazy<MainViewModel> = viewModels()

    override fun getDataBinding(): ActivityMainBinding =
        ActivityMainBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstance: Bundle?) {

        askNotificationPermission()
        getNavHost()
        getFCMToken()

        if (intent.extras != null) {
            //from notification

            val userId = intent.extras!!.getString("userId")
            val userFirebaseQueries = UserFirebaseQueries()
            if (userId != null) {
                userFirebaseQueries.allUserCollectionReference()?.document(userId)?.get()
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            //val model: UserModel = task.result.toObject(UserModel::class.java)
                            NavHandler.instance.toShowOperationsActivity(this)
                            finish()
                        }
                    }
            }
        } else {
            if (ClientPreferences.inst.isFirstLaunch == true)
                NavHandler.instance.toOnboardingActivity(this)
            else observe()
        }
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

    private fun getNavHost() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNav =
            findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNav)
        bottomNav.setupWithNavController(navController)

    }

    private fun getFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("Token", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }
            val fcmToken = task.result
            Log.e("fcmToken", fcmToken.toString())
            ClientPreferences.inst.FCMtoken = fcmToken
            val userFirebaseQueries = UserFirebaseQueries()
            userFirebaseQueries.currentUserDetails()?.update("fcmToken", fcmToken)
        }
    }


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // İzin verildiğinde yapılacak işlemler
        } else {
            // İzin reddedildiğinde yapılacak işlemler
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // İzin zaten verilmişse yapılacak işlemler
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // Kullanıcıya izin talebinin nedenini açıklamak için uygun bir durumdaysa yapılacak işlemler
            } else {
                // İzin talebinde bulun
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    override fun changeTheme() = setTheme(R.style.Theme_TheatreTicket)

}