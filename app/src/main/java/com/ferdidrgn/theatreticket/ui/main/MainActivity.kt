package com.ferdidrgn.theatreticket.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
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
import com.ferdidrgn.theatreticket.tools.showToast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    private lateinit var navController: NavController
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var appUpdateManager: AppUpdateManager
    private val updateType = AppUpdateType.IMMEDIATE //IMMEDIATE (force) or FLEXIBLE (recommend)

    override fun getVM(): Lazy<MainViewModel> = viewModels()

    override fun getDataBinding(): ActivityMainBinding =
        ActivityMainBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstance: Bundle?) {

        getNavHost()

        if (intent.extras != null) {
            //from notification
            //MOCK DATA
            //LOGİN Mİ DEĞİL Mİ KONTROL ET
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
            askNotificationPermission()
            getFCMToken()
            getLogin()
            checkForAppUpdate()
            observe()
        }
    }

    override fun onResume() {
        super.onResume()
        if (updateType == AppUpdateType.IMMEDIATE) {
            appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
                if (info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    appUpdateManager.startUpdateFlowForResult(info, updateType, this, 123)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (updateType == AppUpdateType.FLEXIBLE) {
            appUpdateManager.unregisterListener(installStateUpdatedListener)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 123) {
            if (resultCode != RESULT_OK) {
                showToast(getString(R.string.error_update_failed))
            }
        }
    }

    private val installStateUpdatedListener = InstallStateUpdatedListener { state ->
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            showToast(getString(R.string.success_update_downloaded))
        }
        lifecycleScope.launch {
            delay(5.seconds)
            appUpdateManager.completeUpdate()
        }
    }

    private fun checkForAppUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)

        if (updateType == AppUpdateType.FLEXIBLE) {
            appUpdateManager.registerListener(installStateUpdatedListener)
        }

        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            val isUpdteAvailable = info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            val isUpdateAllowed = when (updateType) {
                AppUpdateType.FLEXIBLE -> info.isFlexibleUpdateAllowed
                AppUpdateType.IMMEDIATE -> info.isImmediateUpdateAllowed
                else -> false
            }
            if (isUpdteAvailable && isUpdateAllowed) {
                appUpdateManager.startUpdateFlowForResult(info, updateType, this, 123)
            }
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

    private fun reviewPopUp() {
        //Mock Data
        /*if (ClientPreferences.inst.reviewStatus.not()) {
            if (ClientPreferences.inst.reviewCounter % 3 == 0) {
                val review = CustomReviewPopUp(this) { isSuccessful ->
                    if (isSuccessful) {
                        ClientPreferences.inst.reviewStatus = true
                    }
                }
                review.show(supportFragmentManager, "CustomReviewPopUp")
            }
        }*/
    }

    private fun getNavHost() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNav =
            findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNav)
        bottomNav.setupWithNavController(navController)

    }

    private fun getLogin() {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val user = Firebase.auth.currentUser

        if (user != null) {
            val userName = user.displayName
            ClientPreferences.inst.userFullName = userName
            ClientPreferences.inst.isGoogleSignIn = true
        }
    }

    private fun getFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("Token", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }
            val fcmToken = task.result
            Log.d("fcmToken", fcmToken.toString())
            ClientPreferences.inst.FCMtoken = fcmToken
            val userFirebaseQueries = UserFirebaseQueries()
            userFirebaseQueries.currentUserDetails()?.update("fcmToken", fcmToken)
        }
    }


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            showToast(getString(R.string.success_permission_granted))
        } else {
            showToast(getString(R.string.permission_denied))
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