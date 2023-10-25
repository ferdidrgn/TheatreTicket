package com.ferdidrgn.theatreticket.ui.main.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.viewModels
import com.ferdidrgn.theatreticket.tools.helpers.MainSliderHandler
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseFragment
import com.ferdidrgn.theatreticket.base.BasePopUp
import com.ferdidrgn.theatreticket.databinding.FragmentHomeBinding
import com.ferdidrgn.theatreticket.tools.builderADS
import com.ferdidrgn.theatreticket.tools.getPositionAndSendHandler2
import com.ferdidrgn.theatreticket.tools.mainScope
import com.google.android.gms.ads.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    private lateinit var handler: MainSliderHandler

    override fun getVM(): Lazy<HomeViewModel> = viewModels()

    override fun getDataBinding(): FragmentHomeBinding =
        FragmentHomeBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        handler = MainSliderHandler()
        builderADS(requireContext(), binding.adView)

        binding.showAllAdapter = ShowsAllAdapter(viewModel)
        binding.showSliderAdapter = ShowsSliderHorizontalAdapter(viewModel)

        observe()
    }

    private fun observe() {

        viewModel.getAllShow()

        viewModel.show.observe(viewLifecycleOwner) { showList ->
            mainScope {
                binding.showSliderAdapter?.differ?.submitList(showList)
                if (showList != null) {
                    binding.indicator.count = showList.size
                }
                binding.vpSlider.getPositionAndSendHandler2(showList, handler) {
                    binding.indicator.selection = it
                }
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null)
                messagePopUp(requireContext(), errorMessage, false)
        }
    }

    private fun addAds() {
        val adRequest = AdRequest.Builder().build()
        val adView = binding.adView
        adView.loadAd(adRequest)
        adView.adListener = object : AdListener() {
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

    private fun messagePopUp(context: Context, message: String, isSuccess: Boolean) {
        val pupUp = BasePopUp(isSuccess = isSuccess)
        pupUp.apply {
            setPositiveText(context.getString(R.string.done))
            setDesc(message)
            setOnPositiveClick {
                dismiss()
            }
        }
        pupUp.show(parentFragmentManager, "")
    }
}