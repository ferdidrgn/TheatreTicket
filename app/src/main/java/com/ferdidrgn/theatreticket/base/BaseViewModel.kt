package com.ferdidrgn.theatreticket.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ferdidrgn.theatreticket.tools.getContext
import com.ferdidrgn.theatreticket.tools.mainScope

open class BaseViewModel: ViewModel() {
    val error = MutableLiveData<Err?>()
    val eventShowOrHideProgress = MutableLiveData<Boolean>()

    fun showLoading() {
        mainScope {
            eventShowOrHideProgress.value = true
        }
    }

    fun hideLoading() {
        mainScope {
            eventShowOrHideProgress.value = false
        }
    }

    fun message(message: Int): String {
        return getContext().let {
            it?.resources?.getString(message).toString()
        }
    }
}