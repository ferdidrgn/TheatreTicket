package com.ferdidrgn.theatreticket.ui.editProfile

import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.repository.UserFirebaseQueries
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(private val userFirebaseQueries: UserFirebaseQueries) :
    BaseViewModel() {

}