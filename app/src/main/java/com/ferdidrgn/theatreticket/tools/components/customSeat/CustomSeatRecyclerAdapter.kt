package com.ferdidrgn.theatreticket.tools.components.customSeat

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.commonModels.dummyData.Seat
import com.ferdidrgn.theatreticket.ui.buyTicket.TicketBuyViewModel

class CustomSeatRecyclerAdapter(
    val context: Context,
    val seatList: List<Seat?>?,
    val ticketBuyViewModel: TicketBuyViewModel
) :
    RecyclerView.Adapter<CustomSeatRecyclerAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //item initalization
        var ll_design: LinearLayout = itemView.findViewById(R.id.ll_design)
        var tvSeatNo: TextView = itemView.findViewById(R.id.tvSeatNo)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_seat_plan, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var checkIsSelected = true
        val seat = seatList?.get(position)
        holder.tvSeatNo.text = seat?.name

        holder.itemView.setOnClickListener {

            if (seatList?.get(position)?.isSelected == false) {
                if (seat != null) {
                    checkIsSelected = ticketBuyViewModel.insertSeatSelection(seat)
                }
                holder.ll_design.setBackgroundColor(if (checkIsSelected) Color.RED else Color.GREEN)
                seatList[position]?.setIsSelected(checkIsSelected)
            } else {
                checkIsSelected = ticketBuyViewModel.removeSeatSelection(seat)
                seatList?.get(position)?.setIsSelected(checkIsSelected)
                holder.ll_design.setBackgroundColor(if (checkIsSelected) Color.RED else Color.GREEN)
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return seatList?.size ?: 0
    }
}