package com.ferdidrgn.theatreticket.ui.main.ticketSearch

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.ferdidrgn.theatreticket.base.BaseFragment
import com.ferdidrgn.theatreticket.databinding.FragmentTicketSearchBinding
import com.ferdidrgn.theatreticket.tools.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TicketSearchFragment: BaseFragment <TicketSearchViewModel, FragmentTicketSearchBinding>(){
    override fun getVM(): Lazy<TicketSearchViewModel> = viewModels()

    override fun getDataBinding(): FragmentTicketSearchBinding = FragmentTicketSearchBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstanceState: Bundle?) {
        showToast("TicketSearchFragment", requireContext())
    }

}