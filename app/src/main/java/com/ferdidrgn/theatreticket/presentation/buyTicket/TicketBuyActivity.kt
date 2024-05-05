package com.ferdidrgn.theatreticket.presentation.buyTicket

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.viewModels
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.util.base.BaseActivity
import com.ferdidrgn.theatreticket.util.base.BasePopUp
import com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData.Show
import com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData.Stage
import com.ferdidrgn.theatreticket.databinding.ActivityTicketBuyBinding
import com.tayfuncesur.curvedbottomsheet.CurvedBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TicketBuyActivity : BaseActivity<TicketBuyViewModel, ActivityTicketBuyBinding>() {
    override fun getVM(): Lazy<TicketBuyViewModel> = viewModels()

    override fun getDataBinding(): ActivityTicketBuyBinding =
        ActivityTicketBuyBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstance: Bundle?) {
        binding.viewModel = viewModel
        binding.customToolbar.backIconOnBackPress(this)
        setAds(binding.adView)
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

        //stage?.seatId?.let { viewModel.getSeat(it) }

        viewModel.isGetSeats.observe(this) {
            viewModel.getSeats(viewModel.seat.value?._id)
        }

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

        viewModel.showDetails.value = show?.name + " " + show?.date + " " + show?.time

        viewModel.buyTicketPopUp.observe(this) {
            ticketBuyPopUp(this)
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

    override fun onResume() {
        super.onResume()
        setAds(binding.adView)
    }

    private fun bottomSheetInit() {
        val displayMetrics = DisplayMetrics()
        this.windowManager.defaultDisplay.getMetrics(displayMetrics)
        CurvedBottomSheet(
            (displayMetrics.widthPixels / 6).toFloat(),
            view = binding.bottomSheet
        ).init()
    }

    private fun getGridLayout() {
        viewModel.seats.observe(this) { seatsList ->
            viewModel.seatColumnCount.observe(this) { columnCount ->
                binding.customSeatPlan.setUpGridLayoutManager(seatsList, columnCount, viewModel)
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

    private fun ticketBuyPopUp(context: Context) {
        val pupUp = BasePopUp()
        pupUp.apply {
            setPositiveText(context.getString(R.string.done))
            setDesc(context.getString(R.string.are_you_serious))
            setOnPositiveClick {
                viewModel.checkTicket()
                dismiss()
            }
            setOnNegativeClick { dismiss() }
        }
        pupUp.show(supportFragmentManager, "")
    }

}