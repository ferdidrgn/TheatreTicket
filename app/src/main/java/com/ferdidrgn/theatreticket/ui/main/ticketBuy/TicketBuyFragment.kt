package com.ferdidrgn.theatreticket.ui.main.ticketBuy

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.ferdidrgn.theatreticket.base.BaseFragment
import com.ferdidrgn.theatreticket.databinding.FragmentTicketBuyBinding
import com.ferdidrgn.theatreticket.tools.ADMIN_BUY_TICKET
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TicketBuyFragment : BaseFragment<TicketBuyViewModel, FragmentTicketBuyBinding>() {

    private var isSellTicketAdmin = false

    override fun getVM(): Lazy<TicketBuyViewModel> = viewModels()

    override fun getDataBinding(): FragmentTicketBuyBinding =
        FragmentTicketBuyBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isSellTicketAdmin = isSellTicketAdmin()
    }

    override fun onCreateFinished(savedInstanceState: Bundle?) {
        TODO("Not yet implemented")
    }


    private fun isSellTicketAdmin(): Boolean {
        return arguments?.getBoolean(ADMIN_BUY_TICKET) ?: false
    }
}