package com.ferdidrgn.theatreticket.ui.showOperations

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.commonModels.dummyData.Show
import com.ferdidrgn.theatreticket.enums.ID
import com.ferdidrgn.theatreticket.enums.Response
import com.ferdidrgn.theatreticket.repository.ShowFirebaseQuieries
import com.ferdidrgn.theatreticket.tools.helpers.LiveEvent
import com.ferdidrgn.theatreticket.tools.mainScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ShowOperationsViewModel @Inject constructor(private val showFirebaseQuieries: ShowFirebaseQuieries) :
    BaseViewModel(), ShowsUpdateDeleteAdapterListener {

    val show = MutableLiveData<List<Show?>?>()
    val deleteClicked = LiveEvent<Show?>()

    val btnAddShowClicked = LiveEvent<Boolean?>()
    val btnCstmDatePickerClick = LiveEvent<Boolean?>()
    val updateShowPopUp = LiveEvent<Boolean?>()
    val deletePopUp = LiveEvent<Boolean?>()
    val updateBottonVisibility = MutableLiveData<Boolean?>()
    val bottomSheetVisibility = MutableLiveData<Boolean?>()
    val imagePermission = MutableLiveData<Boolean?>()

    val name = MutableStateFlow("")
    val desc = MutableStateFlow("")
    val imageUrl = MutableStateFlow<String?>(null)
    val addOrUpdateImgUrl = MutableStateFlow<Uri?>(null)
    val date = MutableStateFlow("")
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

    fun onCstmDatePickerClick() {
        btnCstmDatePickerClick.postValue(true)
    }

    fun onImgClose() {
        bottomSheetVisibility.postValue(false)
    }

    fun fieldClear() {
        name.value = ""
        desc.value = ""
        imageUrl.value = null
        date.value = ""
        price.value = ""
        ageLimit.value = ""
    }

    fun getAllShow() {
        showLoading()
        mainScope {
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
        showLoading()

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
        showLoading()
        mainScope {
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
        showLoading()
        mainScope {
            updateOrAddShowData = Show(
                name = name.value,
                description = desc.value,
                imageUrl = imageUrl.value,
                addOrUpdateImgUrl = addOrUpdateImgUrl.value,
                date = date.value,
                price = price.value,
                ageLimit = ageLimit.value
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
        show?.name?.let { name.value = it }
        show?.description?.let { desc.value = it }
        show.imageUrl?.let { imageUrl.value = it }
        show?.date?.let { date.value = it }
        show?.price?.let { price.value = it }
        show?.ageLimit?.let { ageLimit.value = it }
        updateBottonVisibility.postValue(true)
    }

    override fun onShowsDeleteListener(show: Show) {
        deleteClicked.postValue(show)
        deletePopUp.postValue(true)
    }
}