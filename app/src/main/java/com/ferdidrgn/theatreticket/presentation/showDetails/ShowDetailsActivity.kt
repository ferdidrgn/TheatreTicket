package com.ferdidrgn.theatreticket.presentation.showDetails

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.viewModels
import com.ferdidrgn.theatreticket.util.base.BaseActivity
import com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData.Show
import com.ferdidrgn.theatreticket.databinding.ActivityShowDetailsBinding
import com.ferdidrgn.theatreticket.util.NavHandler
import com.ferdidrgn.theatreticket.util.SHOW
import com.ferdidrgn.theatreticket.util.SHOW_ID
import com.tayfuncesur.curvedbottomsheet.CurvedBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowDetailsActivity : BaseActivity<ShowDetailsViewModel, ActivityShowDetailsBinding>() {

    override fun getVM(): Lazy<ShowDetailsViewModel> = viewModels()

    override fun getDataBinding(): ActivityShowDetailsBinding =
        ActivityShowDetailsBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstance: Bundle?) {
        binding.viewModel = viewModel
        binding.customToolbar.backIconOnBackPress(this)
        setAds(binding.adView)
        bottomSheetInit()
        getDataIntent()
        observe()
    }

    private fun bottomSheetInit() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        CurvedBottomSheet(
            radius = (displayMetrics.widthPixels / 6).toFloat(),
            view = binding.bottomSheet,
            location = CurvedBottomSheet.Location.TOP
        ).init()
    }

    fun observe() {
        viewModel.btnStageOnClick.observe(this) {
            NavHandler.instance.toStageActivity(this, viewModel.stage.value)
        }

        viewModel.btnSeatOnClick.observe(this) {
            NavHandler.instance.toBuyTicketActivity(
                this,
                viewModel.show.value,
                viewModel.stage.value
            )
        }
    }

    override fun onResume() {
        super.onResume()
        setAds(binding.adView)
    }

    private fun getDataIntent() {
        val getShowId = intent.getStringExtra(SHOW_ID)
        getShowId?.let {
            viewModel.getShowId(it)
        }

        val getShowDetails = intent.getSerializableExtra(SHOW) as Show?
        viewModel.show.value = Show(
            _id = getShowDetails?._id,
            name = getShowDetails?.name,
            description = getShowDetails?.description,
            imageUrl = getShowDetails?.imageUrl,
            price = getShowDetails?.price,
            date = getShowDetails?.date,
            time = getShowDetails?.time,
            ageLimit = getShowDetails?.ageLimit,
            stageId = getShowDetails?.stageId
        )

        viewModel.getStageId(getShowDetails?.stageId)
    }
}