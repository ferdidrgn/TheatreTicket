package com.ferdidrgn.theatreticket.ui.showAdd

import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.commonModels.dummyData.Show
import com.ferdidrgn.theatreticket.enums.ID
import com.ferdidrgn.theatreticket.repository.ShowFirebaseQuieries
import com.ferdidrgn.theatreticket.tools.ClientPreferences
import com.ferdidrgn.theatreticket.tools.helpers.LiveEvent
import com.ferdidrgn.theatreticket.tools.mainScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ShowAddViewModel @Inject constructor(private val showFirebaseQuieries: ShowFirebaseQuieries) :
    BaseViewModel() {

    val name = MutableStateFlow("")
    val desc = MutableStateFlow("")
    val imageUrl = MutableStateFlow("")
    val date = MutableStateFlow("")
    val price = MutableStateFlow("")
    val ageLimit = MutableStateFlow("")
    val stage = MutableStateFlow("")
    val players = MutableStateFlow("")
    val addShowPopUp = LiveEvent<Boolean?>()

    var showAdd = Show()

    fun onAddImageClick() {
        //addImage.postValue(true)
    }

    fun onBtnAddShowClick() {
        addShowPopUp.postValue(true)
    }

    fun addShow() {
        showLoading()

        mainScope {
            val id = UUID.randomUUID().toString() + ID.Show.id
            showAdd = Show(
                _id = id,
                name = name.value,
                description = desc.value,
                imageUrl = imageUrl.value,
                date = date.value,
                price = price.value,
                ageLimit = ageLimit.value,
            )
            showFirebaseQuieries.addShow(showAdd) { status ->
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