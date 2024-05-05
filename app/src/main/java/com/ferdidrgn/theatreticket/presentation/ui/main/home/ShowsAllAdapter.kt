package com.ferdidrgn.theatreticket.presentation.ui.main.home

import com.ferdidrgn.theatreticket.base.BaseAdapter
import com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData.Show
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.databinding.ItemShowAllBinding

class ShowsAllAdapter(
    private val showDetailsAdapterListener: ShowDetailsAdapterListener
) : BaseAdapter<ItemShowAllBinding, Show>() {

    override val layoutId: Int = R.layout.item_show_all

    override fun bind(binding: ItemShowAllBinding, item: Show, position: Int) {
        binding.apply {
            show = item
            index = position
            listener = showDetailsAdapterListener
            executePendingBindings()
        }
    }
}

interface ShowDetailsAdapterListener {
    fun onShowDetailsAdapterListener(show: Show?)
}