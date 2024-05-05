package com.ferdidrgn.theatreticket.presentation.main.shop

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.ferdidrgn.theatreticket.util.base.BaseFragment
import com.ferdidrgn.theatreticket.databinding.FragmentShopBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShopFragment : BaseFragment<ShopViewModel, FragmentShopBinding>() {

    override fun getVM(): Lazy<ShopViewModel> = viewModels()

    override fun getDataBinding(): FragmentShopBinding =
        FragmentShopBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        setAds(binding.adView)
    }

    override fun onResume() {
        super.onResume()
        setAds(binding.adView)
    }

}