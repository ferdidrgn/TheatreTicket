package com.ferdidrgn.theatreticket.tools.components.customSeat

import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseAdapter
import com.ferdidrgn.theatreticket.commonModels.dummyData.Seat
import com.ferdidrgn.theatreticket.databinding.ItemSeatPlanBinding

//MOCKDATA
class CustomSeatRecyclerAdapter(
    private val seatAdapterListener: SeatAdapterListener
) : BaseAdapter<ItemSeatPlanBinding, Seat>() {

    override val layoutId: Int = R.layout.item_seat_plan

    override fun bind(binding: ItemSeatPlanBinding, item: Seat, position: Int) {
        binding.apply {
            seat = item
            index = position
            listener = seatAdapterListener
            executePendingBindings()
        }
    }
}

interface SeatAdapterListener {
    fun onSeatAdapterListener(show: Seat?) {}
}