package com.ferdidrgn.theatreticket.presentation.showOperations

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.util.base.BaseViewModel
import com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData.Show
import com.ferdidrgn.theatreticket.util.ID
import com.ferdidrgn.theatreticket.util.Response
import com.ferdidrgn.theatreticket.data.repository.ShowFirebaseQuieries
import com.ferdidrgn.theatreticket.util.helpers.LiveEvent
import com.ferdidrgn.theatreticket.util.mainScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class ShowOperationsViewModel @Inject constructor(private val showFirebaseQuieries: ShowFirebaseQuieries) :
    BaseViewModel(), ShowsUpdateDeleteAdapterListener {

    val show = MutableLiveData<List<Show?>?>()
    val deleteClicked = LiveEvent<Show?>()

    val btnAddShowClicked = LiveEvent<Boolean?>()
    val btnCstmDatePickerDateClick = LiveEvent<Boolean?>()
    val btnCstmDatePickerTimeClick = LiveEvent<Boolean?>()
    val updateShowPopUp = LiveEvent<Boolean?>()
    val deletePopUp = LiveEvent<Boolean?>()
    val updateBottonVisibility = MutableLiveData<Boolean?>()
    val bottomSheetVisibility = MutableLiveData<Boolean?>()
    val imagePermission = MutableLiveData<Boolean?>()

    val _id = LiveEvent<String?>()
    val name = MutableStateFlow("")
    val desc = MutableStateFlow("")
    val imageUrl = MutableStateFlow<String?>(null)
    val addOrUpdateImgUrl = MutableStateFlow<Uri?>(null)
    val date = MutableStateFlow("")
    val time = MutableStateFlow("")
    val price = MutableStateFlow("")
    val ageLimit = MutableStateFlow("")

    var updateOrAddShowData: Show? = null

    fun onBtnBottomSheetCloseClick() {
        bottomSheetVisibility.postValue(false)
    }

    fun onBtnBottomSheetOpenClick() {
        bottomSheetVisibility.postValue(true)
        updateBottonVisibility.postValue(false)
        fieldClear()
    }

    fun onBtnAddShowClick() {
        btnAddShowClicked.postValue(true)
    }

    fun onBtnUpdateShowClick() {
        updateShowPopUp.postValue(true)
    }

    fun onUpdateImageClick() {
        imagePermission.postValue(true)
    }

    fun onCstmDatePickerDateClick() {
        btnCstmDatePickerDateClick.postValue(true)
    }

    fun onCstmDatePickerTimeClick() {
        btnCstmDatePickerTimeClick.postValue(false)
    }

    fun onImgClose() {
        bottomSheetVisibility.postValue(false)
    }

    fun fieldClear() {
        name.value = ""
        desc.value = ""
        imageUrl.value = null
        date.value = ""
        time.value = ""
        price.value = ""
        ageLimit.value = ""
    }

    fun getAllShow() {
        mainScope {
            showLoading()
            showFirebaseQuieries.getShow { status, showList ->
                when (status) {
                    Response.ServerError -> {
                        errorMessage.postValue(message(R.string.error_server))
                        hideLoading()
                    }
                    Response.Empty -> {
                        errorMessage.postValue(message(R.string.error_no_any_show))
                        hideLoading()
                    }
                    Response.ThereIs -> {
                        showList?.let {
                            show.postValue(showList)
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

    private fun addShow() {
        mainScope {
            showLoading()
            val id = UUID.randomUUID().toString() + ID.Show.id
            updateOrAddShowData = Show(
                _id = id,
                name = name.value,
                description = desc.value,
                imageUrl = imageUrl.value,
                addOrUpdateImgUrl = addOrUpdateImgUrl.value,
                date = date.value,
                time = time.value,
                price = price.value,
                ageLimit = ageLimit.value,
            )
            updateOrAddShowData?.let { show ->
                showFirebaseQuieries.addShow(show) { status ->
                    when (status) {
                        true -> {
                            hideLoading()
                            successMessage.postValue(message(R.string.success_add_show))
                            getAllShow()
                        }
                        false -> {
                            hideLoading()
                            errorMessage.postValue(message(R.string.error_server))
                        }
                    }
                }
            }
        }
    }

    fun deleteShow() {
        mainScope {
            showLoading()
            showFirebaseQuieries.deleteShow(deleteClicked.value) { status ->
                when (status) {
                    false -> {
                        hideLoading()
                        errorMessage.postValue(message(R.string.error_server))
                    }
                    true -> {
                        hideLoading()
                        successMessage.postValue(message(R.string.success_delete_show))
                        getAllShow()
                    }
                }
            }
        }
    }

    private fun updateShow() {
        mainScope {
            showLoading()
            updateOrAddShowData = Show(
                _id = _id.value,
                name = name.value,
                description = desc.value,
                imageUrl = imageUrl.value,
                addOrUpdateImgUrl = addOrUpdateImgUrl.value,
                date = date.value,
                time = time.value,
                price = price.value,
                ageLimit = ageLimit.value,
                stageId = updateOrAddShowData?.stageId,
            )

            showFirebaseQuieries.updateShow(updateOrAddShowData) { status ->
                when (status) {
                    false -> {
                        hideLoading()
                        errorMessage.postValue(message(R.string.error_server))
                    }
                    true -> {
                        hideLoading()
                        successMessage.postValue(message(R.string.success_update_show))
                        getAllShow()
                    }
                }
            }
        }
    }

    fun checkRequestFields(isUpdate: Boolean) {
        var isRequiredFieldsDone = true

        if (name.value.isEmpty())
            isRequiredFieldsDone = false

        if (desc.value.length < 3)
            isRequiredFieldsDone = false

        if (date.value.isEmpty())
            isRequiredFieldsDone = false

        if (time.value.isEmpty())
            isRequiredFieldsDone = false

        if (price.value.isEmpty())
            isRequiredFieldsDone = false

        if (ageLimit.value.isEmpty())
            isRequiredFieldsDone = false

        if (isRequiredFieldsDone) {
            if (isUpdate)
                updateShow()
            else
                addShow()
        } else
            errorMessage.postValue(message(R.string.error_little_things))
    }

    override fun onShowsUpdateListener(show: Show) {
        show.let {
            updateOrAddShowData = it
            this.show.postValue(ArrayList<Show?>().apply { add(it) })
            _id.postValue(it._id)
            name.value = it.name ?: ""
            desc.value = it.description ?: ""
            imageUrl.value = it.imageUrl
            date.value = it.date ?: ""
            time.value = it.time ?: ""
            price.value = it.price ?: ""
            ageLimit.value = it.ageLimit ?: ""
        }
        updateBottonVisibility.postValue(true)
    }

    override fun onShowsDeleteListener(show: Show) {
        deleteClicked.postValue(show)
        deletePopUp.postValue(true)
    }
}