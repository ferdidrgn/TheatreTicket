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
import com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData.Seats
import com.ferdidrgn.theatreticket.tools.onClickDelayed
import com.ferdidrgn.theatreticket.ui.buyTicket.TicketBuyViewModel

class CustomSeatRecyclerAdapter(
    val context: Context,
    val seatsList: List<Seats?>?,
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
        var seats = seatsList?.get(position)
        holder.tvSeatNo.text = seats?.name

        holder.itemView.onClickDelayed {

            if (seatsList?.get(position)?.isEmpty == false) {
                seats = Seats(
                    _id = seats?._id,
                    name = seats?.name,
                    isEmpty = true,
                    seatId = seats?.seatId
                )
                ticketBuyViewModel.insertSeatSelection(seats) { response ->
                    holder.ll_design.setBackgroundColor(if (response) Color.GREEN else Color.WHITE)
                    seatsList[position]?.setIsSelected(response)
                }

            } else {
                seats = Seats(
                    _id = seats?._id,
                    name = seats?.name,
                    isEmpty = false,
                    seatId = seats?.seatId
                )
                ticketBuyViewModel.removeSeatSelection(seats) { response ->
                    seatsList?.get(position)?.setIsSelected(!response)
                    holder.ll_design.setBackgroundColor(if (response) Color.WHITE else Color.GREEN)
                }
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
        return seatsList?.size ?: 0
    }
}