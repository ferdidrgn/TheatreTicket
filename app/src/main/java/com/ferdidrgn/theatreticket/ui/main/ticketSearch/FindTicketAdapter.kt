package com.ferdidrgn.theatreticket.ui.main.ticketSearch

import com.ferdidrgn.theatreticket.base.BaseAdapter
import com.ferdidrgn.theatreticket.commonModels.dummyData.Sell
import com.ferdidrgn.theatreticket.databinding.ItemFindTicketBinding
import com.ferdidrgn.theatreticket.R

class FindTicketAdapter(
    private val findTicketdapterListener: FindTicketAdapterListener
) : BaseAdapter<ItemFindTicketBinding, Sell>() {

    override val layoutId: Int = R.layout.item_find_ticket

    override fun bind(binding: ItemFindTicketBinding, item: Sell, position: Int) {
        binding.apply {
            sell = item
            index = position
            //listener = findTicketdapterListener
            executePendingBindings()
        }
    }
}

interface FindTicketAdapterListener {
    fun onFindTicketItemClicked(position: Int)
}