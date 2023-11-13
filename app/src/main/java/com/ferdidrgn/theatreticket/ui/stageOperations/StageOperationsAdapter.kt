package com.ferdidrgn.theatreticket.ui.stageOperations

import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseAdapter
import com.ferdidrgn.theatreticket.commonModels.dummyData.Stage
import com.ferdidrgn.theatreticket.databinding.ItemStageOperationsBinding

class StageOperationsAdapter(
    private val stageDetailsAdapterListener: StageUpdateDeleteAdapterListener
) : BaseAdapter<ItemStageOperationsBinding, Stage>() {

    override val layoutId: Int = R.layout.item_stage_operations

    override fun bind(binding: ItemStageOperationsBinding, item: Stage, position: Int) {
        binding.apply {
            stage = item
            index = position
            listener = stageDetailsAdapterListener
            executePendingBindings()
        }
    }
}

interface StageUpdateDeleteAdapterListener {
    fun onStageUpdateListener(stage: Stage)
    fun onStageDeleteListener(stage: Stage)
}