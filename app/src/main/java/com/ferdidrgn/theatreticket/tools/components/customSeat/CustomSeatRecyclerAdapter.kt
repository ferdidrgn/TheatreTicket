package com.ferdidrgn.theatreticket.tools.components.customSeat

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.ferdidrgn.theatreticket.R

class CustomSeatRecyclerAdapter(val context: Context, val dataList: List<String>) :
    RecyclerView.Adapter<CustomSeatRecyclerAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //item initalization
        var ll_design: LinearLayout = itemView.findViewById(R.id.ll_design)
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
        holder.itemView.setOnClickListener {

            //MOCk DATA

            //is selected is used for selecting the position of item
            /*if (dataList[position].isSelected) {
                dataList[position].setIsSelected(false)
                holder.ll_design.setBackgroundColor(Color.GREEN)
            } else {
                dataList[position].setIsSelected(true)

                holder.ll_design.setBackgroundColor(Color.WHITE)
            }*/
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}