package com.ferdidrgn.theatreticket.ui.onbarding

import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.repository.ShowFirebaseQuieries
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(showFirebaseQuieries: ShowFirebaseQuieries) :
    BaseViewModel() {
}