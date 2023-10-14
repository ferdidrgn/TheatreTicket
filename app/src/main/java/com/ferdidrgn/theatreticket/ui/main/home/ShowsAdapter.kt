package com.ferdidrgn.theatreticket.ui.main.home

import com.ferdidrgn.theatreticket.base.BaseAdapter
import com.ferdidrgn.theatreticket.commonModels.dummyData.Show
import com.ferdidrgn.theatreticket.databinding.ItemFindTicketBinding
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.databinding.ItemShowBinding

class ShowsAdapter(
    private val showDetailsAdapterListener: ShowDetailsAdapterListener
) : BaseAdapter<ItemShowBinding, Show>() {

    override val layoutId: Int = R.layout.item_show

    override fun bind(binding: ItemShowBinding, item: Show, position: Int) {
        binding.apply {
            show = item
            index = position
            //listener = showDetailsAdapterListener
            executePendingBindings()
        }
    }
}

interface ShowDetailsAdapterListener {
    fun onShowDetailsAdapterListener(position: Int)
}