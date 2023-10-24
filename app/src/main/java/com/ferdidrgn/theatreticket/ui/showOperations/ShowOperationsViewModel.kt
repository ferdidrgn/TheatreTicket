package com.ferdidrgn.theatreticket.ui.showOperations

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
    val updateShowPopUp = LiveEvent<Boolean?>()
    val deletePopUp = LiveEvent<Boolean?>()
    val updateBottonVisibility = LiveEvent<Boolean?>()
    val bottomSheetVisibility = MutableLiveData<Boolean>()

    var deleteIndex = 0
    var updateIndex = 0

    val name = MutableStateFlow("")
    val desc = MutableStateFlow("")
    val imageUrl = MutableStateFlow("")
    val date = MutableStateFlow("")
    val price = MutableStateFlow("")
    val ageLimit = MutableStateFlow("")
    val stage = MutableStateFlow("")
    val players = MutableStateFlow("")

    var updateOrAddShowData: Show? = null

    fun onBtnAddShowClick() {
        btnAddShowClicked.postValue(true)
    }

    fun onBtnUpdateShowClick() {
        updateShowPopUp.postValue(true)
    }

    fun onUpdateImageClick() {
        //dosyaları açan pop up ekle
    }

    fun getAllShow() {
        showLoading()
        mainScope {
            showFirebaseQuieries.getShow() { status, showList ->
                when (status) {
                    Response.ServerError.response -> {
                        errorMessage.postValue(message(R.string.error_server))
                        hideLoading()
                    }
                    Response.Empty.response -> {
                        errorMessage.postValue(message(R.string.error_no_any_show))
                        hideLoading()
                    }
                    Response.ThereIs.response -> {
                        showList?.let {
                            show.postValue(showList)
                        }
                        hideLoading()
                    }
                }
            }
        }
    }

    fun addShow() {
        showLoading()

        mainScope {
            val id = UUID.randomUUID().toString() + ID.Show.id
            updateOrAddShowData = Show(
                _id = id,
                name = name.value,
                description = desc.value,
                imageUrl = imageUrl.value,
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
                        show.value?.toMutableList()?.removeAt(deleteIndex)
                        show.postValue(show.value)
                        hideLoading()
                        successMessage.postValue(message(R.string.success_delete_show))
                    }
                }
            }
        }
    }

    fun updateShow() {
        showLoading()
        mainScope {
            updateOrAddShowData = Show(
                name = name.value,
                description = desc.value,
                imageUrl = imageUrl.value,
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
                        show.value?.toMutableList()?.set(updateIndex, updateOrAddShowData)
                        show.postValue(show.value)
                        hideLoading()
                        successMessage.postValue(message(R.string.success_update_show))
                    }
                }
            }
        }
    }

    override fun onShowsUpdateListener(position: Int, show: Show) {
        updateIndex = position
        show?.name?.let { name.value = it }
        show?.description?.let { desc.value = it }
        show.imageUrl?.let { imageUrl.value = it }
        show?.date?.let { date.value = it }
        show?.price?.let { price.value = it }
        show?.ageLimit?.let { ageLimit.value = it }
        updateBottonVisibility.postValue(true)
    }

    override fun onShowsDeleteListener(position: Int, show: Show) {
        deleteIndex = position
        deleteClicked.postValue(show)
        deletePopUp.postValue(true)
    }
}