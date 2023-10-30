package com.ferdidrgn.theatreticket.ui.showDetails

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.viewModels
import com.ferdidrgn.theatreticket.base.BaseActivity
import com.ferdidrgn.theatreticket.commonModels.dummyData.Show
import com.ferdidrgn.theatreticket.databinding.ActivityShowDetailsBinding
import com.ferdidrgn.theatreticket.tools.SHOW
import com.ferdidrgn.theatreticket.tools.builderADS
import com.tayfuncesur.curvedbottomsheet.CurvedBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowDetailsActivity : BaseActivity<ShowDetailsViewModel, ActivityShowDetailsBinding>() {

    override fun getVM(): Lazy<ShowDetailsViewModel> = viewModels()

    override fun getDataBinding(): ActivityShowDetailsBinding =
        ActivityShowDetailsBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstance: Bundle?) {
        binding.viewModel = viewModel
        builderADS(this, binding.adView)
        bottomSheetInit()
        getDataIntent()
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

    private fun getDataIntent() {
        val getShowDetails = intent.getSerializableExtra(SHOW) as ArrayList<Show?>
        getShowDetails.forEach {
            viewModel.show.value = Show(
                _id = it?._id,
                name = it?.name,
                description = it?.description,
                imageUrl = it?.imageUrl,
                price = it?.price,
                date = it?.date,
                ageLimit = it?.ageLimit,
            )
        }
    }

}