package com.ferdidrgn.theatreticket.presentation.ui.main

import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.data.repository.ShowFirebaseQuieries
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val showFirebaseQuieries: ShowFirebaseQuieries) :
    BaseViewModel() {
}