package com.ferdidrgn.theatreticket.presentation.main.shop

import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.util.base.BaseAdapter
import com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData.Show
import com.ferdidrgn.theatreticket.databinding.ItemShopBinding

class ShopAdapter(
    private val shopDetailsAdapterListener: ShopDetailsAdapterListener
) : BaseAdapter<ItemShopBinding, Show>() {

    override val layoutId: Int = R.layout.item_shop

    override fun bind(binding: ItemShopBinding, item: Show, position: Int) {
        binding.apply {
            shop = item
            index = position
            listener = shopDetailsAdapterListener
            executePendingBindings()
        }
    }
}

interface ShopDetailsAdapterListener {
    fun shopDetailsAdapterListener(show: Show?)
}