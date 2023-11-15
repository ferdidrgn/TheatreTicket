package com.ferdidrgn.theatreticket.ui.buyTicket

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.viewModels
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseActivity
import com.ferdidrgn.theatreticket.base.BasePopUp
import com.ferdidrgn.theatreticket.commonModels.dummyData.Show
import com.ferdidrgn.theatreticket.commonModels.dummyData.Stage
import com.ferdidrgn.theatreticket.databinding.ActivityTicketBuyBinding
import com.tayfuncesur.curvedbottomsheet.CurvedBottomSheet

class TicketBuyActivity : BaseActivity<TicketBuyActivityViewModel, ActivityTicketBuyBinding>() {
    override fun getVM(): Lazy<TicketBuyActivityViewModel> = viewModels()

    override fun getDataBinding(): ActivityTicketBuyBinding =
        ActivityTicketBuyBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstance: Bundle?) {
        binding.viewModel = viewModel
        //mock data
        //builderADS()
        bottomSheetInit()
        getGridLayout()
        observe()
    }

    private fun observe() {
        val show = intent.getSerializableExtra("show") as Show?
        val stage = intent.getSerializableExtra("stage") as Stage?


        viewModel.stage.value = Stage(
            _id = stage?._id,
            name = stage?.name,
            description = stage?.description,
            communication = stage?.communication,
            capacity = stage?.capacity,
            address = stage?.address,
            imgUrl = stage?.imgUrl,
            locationLat = stage?.locationLat,
            locationLng = stage?.locationLng,
            seatId = stage?.seatId
        )

        viewModel.show.value = Show(
            _id = show?._id,
            name = show?.name,
            imageUrl = show?.imageUrl,
            description = show?.description,
            stageId = show?.stageId,
            date = show?.date,
            time = show?.time,
            price = show?.price,
            ageLimit = show?.ageLimit,
            seatId = show?.seatId,
        )

        viewModel.btnCstmDatePickerClick.observe(this) {
            binding.cdpDate.setCustomDataPickerClick()
        }
        viewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage != null)
                messagePopUp(this, errorMessage, false)
        }
        viewModel.successMessage.observe(this) { successMessage ->
            if (successMessage != null)
                messagePopUp(this, successMessage, true)
        }
    }

    private fun bottomSheetInit() {
        val displayMetrics = DisplayMetrics()
        this.windowManager.defaultDisplay.getMetrics(displayMetrics)
        CurvedBottomSheet(
            (displayMetrics.widthPixels / 6).toFloat(),
            view = binding.bottomSheet
        ).init()
    }

    fun getGridLayout() {
        viewModel.seat.observe(this) { seatList ->
            viewModel.seatColumnCount.observe(this) { columnCount ->
                // binding.customSeatPlan.setUpGridLayoutManager(seatList, columnCount)
            }
        }
    }

    private fun messagePopUp(context: Context, message: String, isSuccess: Boolean) {
        val pupUp = BasePopUp(isSuccess = isSuccess)
        pupUp.apply {
            setPositiveText(context.getString(R.string.done))
            setDesc(message)
            setOnPositiveClick {
                dismiss()
            }
        }
        pupUp.show(supportFragmentManager, "")
    }
}