package com.ferdidrgn.theatreticket.ui.main.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.viewModels
import com.ferdidrgn.theatreticket.base.BaseFragment
import com.ferdidrgn.theatreticket.databinding.FragmentHomeBinding
import com.ferdidrgn.theatreticket.tools.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment: BaseFragment<HomeViewModel, FragmentHomeBinding>(){
    override fun getVM(): Lazy<HomeViewModel> = viewModels()

    override fun getDataBinding(): FragmentHomeBinding =
        FragmentHomeBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstanceState: Bundle?) {
        showToast("HomeFragment",requireContext())
    }

}