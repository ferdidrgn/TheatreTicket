package com.ferdidrgn.theatreticket.presentation.onboarding

import androidx.lifecycle.MutableLiveData
import com.ferdidrgn.theatreticket.util.base.BaseViewModel
import com.ferdidrgn.theatreticket.data.repository.ShowFirebaseQuieries
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(showFirebaseQuieries: ShowFirebaseQuieries) :
    BaseViewModel() {

    val isPageIndicatorNext = MutableLiveData<Boolean?>()
    val getLoginActivity = MutableLiveData<Boolean?>()
    val getTermsConditionActivity = MutableLiveData<Boolean?>()

    fun onRightArrowClick() {
        isPageIndicatorNext.postValue(true)
    }

    fun onLeftArrowClick() {
        isPageIndicatorNext.postValue(false)
    }

    fun onLoginActivityClick() {
        getLoginActivity.postValue(true)
    }

    fun onTermsConditionClicked() {
        getTermsConditionActivity.postValue(true)
    }
}