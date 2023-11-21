package com.ferdidrgn.theatreticket.ui.main.shop

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.ferdidrgn.theatreticket.base.BaseFragment
import com.ferdidrgn.theatreticket.databinding.FragmentShopBinding
import com.ferdidrgn.theatreticket.tools.builderADS
import com.ferdidrgn.theatreticket.tools.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShopFragment : BaseFragment<ShopViewModel, FragmentShopBinding>() {

    override fun getVM(): Lazy<ShopViewModel> = viewModels()

    override fun getDataBinding(): FragmentShopBinding =
        FragmentShopBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        builderADS(requireContext(), binding.adView)
    }

}