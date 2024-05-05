package com.ferdidrgn.theatreticket.util.components.customSeat

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData.Seats
import com.ferdidrgn.theatreticket.presentation.buyTicket.TicketBuyViewModel

class CustomSeatPlan : ConstraintLayout {
    lateinit var rvSeat: RecyclerView
    private lateinit var customSeatRecyclerAdapter: CustomSeatRecyclerAdapter

    constructor(context: Context) : super(context) {
        initLayout(context, null, null)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initLayout(context, attributeSet, null)
    }

    constructor(context: Context, attributeSet: AttributeSet, style: Int) : super(
        context,
        attributeSet,
        style
    ) {
        initLayout(context, attributeSet, style)
    }

    private fun initLayout(context: Context, attributeSet: AttributeSet?, style: Int?) {
        ConstraintLayout.inflate(context, R.layout.custom_seat_plan, this)
        rvSeat = findViewById(R.id.rvSeat)
    }

    fun setUpGridLayoutManager(list: List<Seats?>?, column: Int, viewModel: TicketBuyViewModel) {

        //grid layout manager

        customSeatRecyclerAdapter = CustomSeatRecyclerAdapter(context, list, viewModel)
        rvSeat.adapter = customSeatRecyclerAdapter
        rvSeat.layoutManager = GridLayoutManager(context, column, GridLayoutManager.VERTICAL, false)

    }
}