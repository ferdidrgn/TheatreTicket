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

class TestCustomSeatRecyclerAdapter(val context: Context, val seatList: List<Seat?>?) :
    RecyclerView.Adapter<TestCustomSeatRecyclerAdapter.MyViewHolder>() {

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
        holder.tvSeatNo.text = seatList?.get(position)?.name


        holder.itemView.setOnClickListener {

            //is selected is used for selecting the position of item
            if (seatList?.get(position)?.isSelected == true) {
                seatList[position]?.setIsSelected(false)
                holder.ll_design.setBackgroundColor(Color.GREEN)
            } else {
                seatList?.get(position)?.setIsSelected(true)
                holder.ll_design.setBackgroundColor(Color.WHITE)
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