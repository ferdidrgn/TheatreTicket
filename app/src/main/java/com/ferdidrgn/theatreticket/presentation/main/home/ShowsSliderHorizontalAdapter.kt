package com.ferdidrgn.theatreticket.presentation.main.home

import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.util.base.BaseAdapter
import com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData.Show
import com.ferdidrgn.theatreticket.databinding.ItemShowSliderBinding

class ShowsSliderHorizontalAdapter(
    private val showSliderDetailsAdapterListener: ShowSliderDetailsAdapterListener
) : BaseAdapter<ItemShowSliderBinding, Show>() {

    override val layoutId: Int = R.layout.item_show_slider

    override fun bind(binding: ItemShowSliderBinding, item: Show, position: Int) {
        binding.apply {
            show = item
            index = position
            listener = showSliderDetailsAdapterListener
            executePendingBindings()
        }
    }
}

interface ShowSliderDetailsAdapterListener {
    fun onShowSliderDetailsAdapterListener(show: Show)
}