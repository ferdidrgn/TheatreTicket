package com.ferdidrgn.theatreticket.ui.showUpdateDelete

import com.ferdidrgn.theatreticket.base.BaseAdapter
import com.ferdidrgn.theatreticket.commonModels.dummyData.Show
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.databinding.ItemShowUpdateDeleteBinding

class ShowsUpdateDeleteAdapter(
    private val showDetailsAdapterListener: ShowsUpdateDeleteAdapterListener
) : BaseAdapter<ItemShowUpdateDeleteBinding, Show>() {

    override val layoutId: Int = R.layout.item_show_update_delete

    override fun bind(binding: ItemShowUpdateDeleteBinding, item: Show, position: Int) {
        binding.apply {
            show = item
            index = position
            listener = showDetailsAdapterListener
            executePendingBindings()
        }
    }
}

interface ShowsUpdateDeleteAdapterListener {
    fun onShowsUpdateListener(position: Int, show: Show)
    fun onShowsDeleteListener(position: Int, show: Show)
}