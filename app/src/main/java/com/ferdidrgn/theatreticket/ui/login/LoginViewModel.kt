package com.ferdidrgn.theatreticket.ui.login

import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.repository.ShowFirebaseQuieries
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(showFirebaseQuieries: ShowFirebaseQuieries) :
    BaseViewModel() {
}