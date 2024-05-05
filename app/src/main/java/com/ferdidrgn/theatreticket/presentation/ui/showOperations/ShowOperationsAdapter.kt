package com.ferdidrgn.theatreticket.presentation.ui.showOperations

import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseAdapter
import com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData.Show
import com.ferdidrgn.theatreticket.databinding.ItemShowOperationsBinding

class ShowOperationsAdapter(
    private val showDetailsAdapterListener: ShowsUpdateDeleteAdapterListener
) : BaseAdapter<ItemShowOperationsBinding, Show>() {

    override val layoutId: Int = R.layout.item_show_operations

    override fun bind(binding: ItemShowOperationsBinding, item: Show, position: Int) {
        binding.apply {
            show = item
            index = position
            listener = showDetailsAdapterListener
            executePendingBindings()
        }
    }
}

interface ShowsUpdateDeleteAdapterListener {
    fun onShowsUpdateListener(show: Show)
    fun onShowsDeleteListener(show: Show)
}