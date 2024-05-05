package com.ferdidrgn.theatreticket.presentation.stage

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.util.base.BaseActivity
import com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData.Stage
import com.ferdidrgn.theatreticket.databinding.ActivityStageBinding
import com.ferdidrgn.theatreticket.util.STAGE
import com.ferdidrgn.theatreticket.util.bitMapFromVector
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StageActivity : BaseActivity<StageViewModel, ActivityStageBinding>(), OnMapReadyCallback {

    private lateinit var mGoogleMap: GoogleMap
    private var latitude = 0.0
    private var longitude = 0.0
    var bm: Bitmap? = null

    override fun getVM(): Lazy<StageViewModel> = viewModels()

    override fun getDataBinding(): ActivityStageBinding =
        ActivityStageBinding.inflate(layoutInflater)

    override fun onCreateFinished(savedInstance: Bundle?) {
        binding.viewModel = viewModel
        binding.customToolbar.backIconOnBackPress(this)
        setAds(binding.adView)

        observe()
    }

    fun observe() {
        val getStageDetails = intent.getSerializableExtra(STAGE) as Stage?
        viewModel.stage.value = Stage(
            _id = getStageDetails?._id,
            name = getStageDetails?.name,
            description = getStageDetails?.description,
            communication = getStageDetails?.communication,
            capacity = getStageDetails?.capacity,
            address = getStageDetails?.address,
            imgUrl = getStageDetails?.imgUrl,
            locationLat = getStageDetails?.locationLat,
            locationLng = getStageDetails?.locationLng,
            seatId = getStageDetails?.seatId
        )

        latitude = viewModel.stage.value?.locationLat ?: 0.0
        longitude = viewModel.stage.value?.locationLng ?: 0.0

        viewModel.stage.value?.imgUrl.let { stageImg ->
            if (stageImg != null) {
                /*getBitmap(stageImg, this@StageActivity) {
                    CoroutineScope(Dispatchers.Main).launch {
                        bm = it?.let { bitmap ->
                            getMarkerBitmapFromView(bitmap)
                        }
                    }
                }*/
            }
        }
        binding.expandedMap?.getMapAsync(this@StageActivity)
    }


    override fun onMapReady(p0: GoogleMap) {

        mGoogleMap = p0
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 15f))
        CoroutineScope(Dispatchers.Main).launch {
            delay(1000)
            mGoogleMap.addMarker(
                MarkerOptions()
                    .icon(bm?.let { image -> BitmapDescriptorFactory.fromBitmap(image) }
                        ?: kotlin.run {
                            bitMapFromVector(
                                this@StageActivity,
                                R.drawable.ic_gps_location_navigation_pin
                            )
                        })
                    .position(LatLng(latitude, longitude))
            )
        }
        mGoogleMap.setOnMarkerClickListener {
            openGoogleMap()
            false
        }
    }

    private fun openGoogleMap() {
        val callBottomSheet = GetDirectionsBottomSheet(longitude, latitude)
        callBottomSheet.show(supportFragmentManager, "")
    }

    private fun getMarkerBitmapFromView(bm: Bitmap?): Bitmap? {
        val customMarkerView = binding.mapMarker
        val markerImageView = binding.mapMarker.imgStageInMap

        markerImageView.setImageBitmap(bm)
        customMarkerView.root.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        customMarkerView.root.layout(
            0,
            0,
            customMarkerView.root.measuredWidth,
            customMarkerView.root.measuredHeight
        )
        customMarkerView.root.buildDrawingCache()
        val returnedBitmap = Bitmap.createBitmap(
            customMarkerView.root.measuredWidth, customMarkerView.root.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(returnedBitmap)
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
        val drawable = customMarkerView.root.background
        drawable?.draw(canvas)
        customMarkerView.root.draw(canvas)
        return returnedBitmap
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CoroutineScope(Dispatchers.Main).launch {
            binding.expandedMap?.onCreate(savedInstanceState)
        }
    }

    override fun onStart() {
        CoroutineScope(Dispatchers.Main).launch {
            binding.expandedMap?.onStart()
        }
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.Main).launch {
            binding.expandedMap?.onResume()
        }

        setAds(binding.adView)
    }

    override fun onPause() {
        binding.expandedMap?.onPause()
        super.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        CoroutineScope(Dispatchers.Main).launch {
            binding.expandedMap?.onLowMemory()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.expandedMap?.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (binding.expandedMap != null) {
            binding.expandedMap?.onDestroy()
        }
    }

    override fun onStop() {
        binding.expandedMap?.onStop()
        super.onStop()
    }
}