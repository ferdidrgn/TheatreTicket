package com.ferdidrgn.theatreticket.ui.showDetails

import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.repository.ShowFirebaseQuieries
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShowDetailsViewModel @Inject constructor(
    private val showFirebaseQuieries: ShowFirebaseQuieries
) : BaseViewModel() {

}