package com.ferdidrgn.theatreticket.data.repository

import com.ferdidrgn.theatreticket.tools.enums.Response

interface AppToolsFireBaseQueriesRepository {
    fun getTermsConditionOrPrivacyPolicy(
        whichTermsAndPrivacy: String,
        status: (Response, String?) -> Unit
    ): Unit

    fun getContactUsEmail(status: (Response, String?) -> Unit): Unit
}