package com.ferdidrgn.theatreticket.presentation.language

import androidx.lifecycle.MutableLiveData
import com.ferdidrgn.theatreticket.util.base.BaseViewModel
import com.ferdidrgn.theatreticket.util.ContextLanguages
import com.ferdidrgn.theatreticket.util.Languages
import com.ferdidrgn.theatreticket.data.repository.ShowFirebaseQuieries
import com.ferdidrgn.theatreticket.util.ClientPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(private val showFirebaseQuieries: ShowFirebaseQuieries) :
    BaseViewModel() {

    val whichButtonSelected = MutableLiveData<Boolean>()

    var selected: (() -> Unit)? = null

    fun firstState() {
        whichButtonSelected.postValue(false)
        if (ClientPreferences.inst.language == Languages.TURKISH.language) {
            whichButtonSelected.postValue(false)
        } else {
            whichButtonSelected.postValue(true)
        }
    }

    fun turkishLanguageItemClicked() {
        whichButtonSelected.postValue(false)
        ClientPreferences.inst.language = Languages.TURKISH.language
        ClientPreferences.inst.contextLanguage = ContextLanguages.TURKISH.language
        selected?.invoke()
    }

    fun englishLanguageItemClicked() {
        whichButtonSelected.postValue(true)
        ClientPreferences.inst.language = Languages.English.language
        ClientPreferences.inst.contextLanguage = ContextLanguages.English.language
        selected?.invoke()
    }
}