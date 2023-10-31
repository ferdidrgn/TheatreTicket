package com.ferdidrgn.theatreticket.ui.onbarding.onboardingFragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.ferdidrgn.theatreticket.base.BaseFragment
import com.ferdidrgn.theatreticket.databinding.FragmentOnboardingThirdBinding
import com.ferdidrgn.theatreticket.tools.NavHandler
import com.ferdidrgn.theatreticket.ui.onbarding.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingThirdFragment :
    BaseFragment<OnboardingViewModel, FragmentOnboardingThirdBinding>() {
    override fun getVM(): Lazy<OnboardingViewModel> = viewModels()

    override fun getDataBinding(): FragmentOnboardingThirdBinding =
        FragmentOnboardingThirdBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstanceState: Bundle?) {
        binding.viewModel = viewModel

        askNotificationPermission()

        viewModel.getTermsConditionActivity.observe(viewLifecycleOwner) {
            NavHandler.instance.toTermsAndConditionsActivity(requireContext())
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
                    requireContext(),
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

}