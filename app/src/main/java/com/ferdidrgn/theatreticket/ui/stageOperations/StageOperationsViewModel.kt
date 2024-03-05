package com.ferdidrgn.theatreticket.ui.stageOperations

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.commonModels.dummyData.Show
import com.ferdidrgn.theatreticket.commonModels.dummyData.Stage
import com.ferdidrgn.theatreticket.enums.ID
import com.ferdidrgn.theatreticket.enums.Response
import com.ferdidrgn.theatreticket.repository.StageFirebaseQueries
import com.ferdidrgn.theatreticket.tools.helpers.LiveEvent
import com.ferdidrgn.theatreticket.tools.mainScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*
import javax.inject.Inject

@HiltViewModel
class StageOperationsViewModel @Inject constructor(private val stageFirebaseQueries: StageFirebaseQueries) :
    BaseViewModel(), StageUpdateDeleteAdapterListener {

    val stage = MutableLiveData<List<Stage?>?>()

    val _id = LiveEvent<String?>()
    var name = MutableStateFlow("")
    var imgUrl = MutableStateFlow("")
    val addOrUpdateImgUrl = MutableStateFlow<Uri?>(null)
    var description = MutableStateFlow("")
    var communication = MutableStateFlow("")
    var capacity = MutableStateFlow("")
    var address = MutableStateFlow("")
    val locationLat = MutableStateFlow("")
    val locationLng = MutableStateFlow("")
    val seatId = MutableStateFlow("")
    var seatColumnCount = MutableStateFlow("")
    var seatRowCount = MutableStateFlow("")


    val updateBottonVisibility = MutableLiveData<Boolean?>()
    val bottomSheetVisibility = MutableLiveData<Boolean?>()
    val updateStagePopUp = LiveEvent<Boolean?>()
    val deletePopUp = LiveEvent<Boolean?>()

    val imagePermission = MutableLiveData<Boolean?>()
    val deleteClicked = LiveEvent<Stage?>()
    val btnAddStageClicked = LiveEvent<Boolean?>()


    var updateOrAddStageData: Stage? = null


    fun getAllStage() {
        mainScope {
            showLoading()
            stageFirebaseQueries.getStage { status, stageList ->
                when (status) {
                    Response.ServerError -> {
                        errorMessage.postValue(message(R.string.error_server))
                        hideLoading()
                    }
                    Response.Empty -> {
                        errorMessage.postValue(message(R.string.error_no_any_stage))
                        hideLoading()
                    }
                    Response.ThereIs -> {
                        stageList?.let {
                            stage.postValue(stageList)
                        }
                        hideLoading()
                    }
                    else -> {
                        errorMessage.postValue(message(R.string.error))
                        hideLoading()
                    }
                }
            }
        }
    }

    private fun updateStage() {
        mainScope {
            showLoading()
            updateOrAddStageData = Stage(
                _id = _id.value,
                name = name.value,
                imgUrl = imgUrl.value,
                description = description.value,
                communication = communication.value,
                capacity = capacity.value,
                address = address.value,
                locationLat = locationLat.value.toDouble(),
                locationLng = locationLng.value.toDouble(),
                //seatId = seatId.value,
                //seatColumnCount = seatColumnCount.value.toInt(),
                //seatRowCount = seatRowCount.value.toInt()
            )
            stageFirebaseQueries.updateStage(updateOrAddStageData) { status ->
                when (status) {
                    true -> {
                        hideLoading()
                        successMessage.postValue(message(R.string.success_update_stage))
                        getAllStage()
                    }
                    false -> {
                        hideLoading()
                        errorMessage.postValue(message(R.string.error_server))
                    }
                }
            }
        }
    }

    private fun addStage() {

        mainScope {
            showLoading()
            val id = UUID.randomUUID().toString() + ID.Stage.id
            updateOrAddStageData = Stage(
                _id = id,
                name = name.value,
                imgUrl = imgUrl.value,
                description = description.value,
                communication = communication.value,
                capacity = capacity.value,
                address = address.value,
                locationLat = locationLat.value.toDouble(),
                locationLng = locationLng.value.toDouble(),
                //seatId = seatId.value,
                //seatColumnCount = seatColumnCount.value.toInt(),
                //seatRowCount = seatRowCount.value.toInt()
            )

            stageFirebaseQueries.addStage(updateOrAddStageData) { status ->
                when (status) {
                    true -> {
                        hideLoading()
                        successMessage.postValue(message(R.string.success_add_stage))
                        getAllStage()
                    }
                    false -> {
                        hideLoading()
                        errorMessage.postValue(message(R.string.error_server))
                    }
                }
            }
        }
    }

    fun deleteStage() {
        mainScope {
            showLoading()
            stageFirebaseQueries.deleteStage(deleteClicked.value) { status ->
                when (status) {
                    false -> {
                        hideLoading()
                        errorMessage.postValue(message(R.string.error_server))
                    }
                    true -> {
                        hideLoading()
                        successMessage.postValue(message(R.string.success_delete_stage))
                        getAllStage()
                    }
                }
            }
        }
    }

    fun checkRequestFields(isUpdate: Boolean) {
        var isRequiredFieldsDone = true

        if (name.value.isEmpty())
            isRequiredFieldsDone = false

        if (description.value.length < 3)
            isRequiredFieldsDone = false

        if (communication.value.isEmpty())
            isRequiredFieldsDone = false

        if (capacity.value.isEmpty())
            isRequiredFieldsDone = false

        if (address.value.isEmpty())
            isRequiredFieldsDone = false

        if (locationLat.value.isEmpty())
            isRequiredFieldsDone = false
        else {
            try {
                locationLat.value.toDouble()
            } catch (e: Exception) {
                isRequiredFieldsDone = false
            }
        }

        if (locationLng.value.isEmpty())
            isRequiredFieldsDone = false
        else {
            try {
                locationLng.value.toDouble()
            } catch (e: Exception) {
                isRequiredFieldsDone = false
            }
        }

        if (isRequiredFieldsDone) {
            if (isUpdate)
                updateStage()
            else
                addStage()
        } else
            errorMessage.postValue(message(R.string.error_little_things))
    }

    fun fieldClear() {
        name.value = ""
        imgUrl.value = ""
        description.value = ""
        communication.value = ""
        capacity.value = ""
        address.value = ""
        locationLat.value = ""
        locationLng.value = ""
        seatColumnCount.value = ""
        seatRowCount.value = ""
    }

//Click Event

    fun onBtnBottomSheetCloseClick() {
        bottomSheetVisibility.postValue(false)
    }

    fun onBtnBottomSheetOpenClick() {
        bottomSheetVisibility.postValue(true)
        updateBottonVisibility.postValue(false)
        fieldClear()
    }

    fun onBtnAddStageClick() {
        btnAddStageClicked.postValue(true)
    }

    fun onBtnUpdateStageClick() {
        updateStagePopUp.postValue(true)
    }

    fun onUpdateImageClick() {
        imagePermission.postValue(true)
    }

    override fun onStageUpdateListener(stage: Stage) {
        stage?.let {
            this.stage.postValue(ArrayList<Stage?>().apply { add(it) })
            updateOrAddStageData = it
            _id.postValue(it._id.toString())
            name.value = it.name.toString()
            imgUrl.value = it.imgUrl.toString()
            description.value = it.description.toString()
            communication.value = it.communication.toString()
            capacity.value = it.capacity.toString()
            address.value = it.address.toString()
            locationLat.value = it.locationLat.toString()
            locationLng.value = it.locationLng.toString()
            seatColumnCount.value = it.seatColumnCount.toString()
            seatRowCount.value = it.seatRowCount.toString()
        }
        updateBottonVisibility.postValue(true)
    }

    override fun onStageDeleteListener(stage: Stage) {
        deleteClicked.postValue(stage)
        deletePopUp.postValue(true)
    }
}