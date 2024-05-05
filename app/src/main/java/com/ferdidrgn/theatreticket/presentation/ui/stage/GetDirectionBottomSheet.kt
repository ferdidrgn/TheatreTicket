package com.ferdidrgn.theatreticket.presentation.ui.stage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.viewModels
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseBottomSheet
import com.ferdidrgn.theatreticket.databinding.GetDirectionBottomSheetBinding
import com.ferdidrgn.theatreticket.ui.main.MainViewModel

class GetDirectionsBottomSheet(val longitude: Double, val latitude: Double) :
    BaseBottomSheet<MainViewModel, GetDirectionBottomSheetBinding>() {

    override fun getVM(): Lazy<MainViewModel> = viewModels()

    override fun getDataBinding(): GetDirectionBottomSheetBinding =
        GetDirectionBottomSheetBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstance: Bundle?) {
        binding.clGetDirector.setOnClickListener {
            navigateToMap()
        }
        binding.clCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun navigateToMap() {
        val strUri =
            resources.getString(R.string.http_maps) + "${latitude},${longitude}"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))
        intent.setClassName(
            resources.getString(R.string.package_google_map),
            resources.getString(R.string.class_google_maps)
        )
        startActivity(intent)
    }
}
