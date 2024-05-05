package com.ferdidrgn.theatreticket.domain.repository

import com.ferdidrgn.theatreticket.util.Response

interface AppToolsFireBaseQueriesRepository {
    fun getTermsConditionOrPrivacyPolicy(
        whichTermsAndPrivacy: String,
        status: (Response, String?) -> Unit
    ): Unit

    fun getContactUsEmail(status: (Response, String?) -> Unit): Unit
}