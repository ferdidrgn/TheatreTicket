package com.ferdidrgn.theatreticket.tools.components.customSeat

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ferdidrgn.theatreticket.R

class CustomSeatPlan : ConstraintLayout {
    lateinit var rvSeat: RecyclerView
    private lateinit var customSeatRecyclerAdapter: CustomSeatRecyclerAdapter
    val dataList = mutableListOf<String>()

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

    fun gridLayoutManager(column: Int, orientation: Int? = null, row: Int? = null) {

        //grid layout manager
        dataList()

        rvSeat.layoutManager = GridLayoutManager(context, column, GridLayoutManager.VERTICAL, false)
        customSeatRecyclerAdapter = CustomSeatRecyclerAdapter(context, dataList)
        rvSeat.adapter = customSeatRecyclerAdapter
    }

    private fun dataList() {

        dataList.add("1X divice")
        dataList.add("1X divice")
        dataList.add("1X divice")
        dataList.add("1X divice")
        dataList.add("5X divice")
        dataList.add("5X divice")
        dataList.add("5X divice")
        dataList.add("5X divice")
        dataList.add("5X divice")
        dataList.add("10X divice")
        dataList.add("10X divice")
        dataList.add("10X divice")
        dataList.add("10X divice")
        dataList.add("10X divice")
        dataList.add("15X divice")
        dataList.add("15X divice")
        dataList.add("15X divice")
        dataList.add("15X divice")
        dataList.add("15X divice")
        dataList.add("20X divice")
        dataList.add("20X divice")
        dataList.add("20X divice")
        dataList.add("20X divice")
        dataList.add("20X divice")
        dataList.add("25X divice")
        dataList.add("25X divice")
        dataList.add("20X divice")
        dataList.add("20X divice")
        dataList.add("20X divice")
        dataList.add("30X divice")
        dataList.add("30X divice")
        dataList.add("30X divice")
        dataList.add("30X divice")
        dataList.add("30X divice")
        dataList.add("35X divice")
        dataList.add("36X divice")
    }
}