package com.ferdidrgn.theatreticket.domain.useCase.termsConditionPrivacyPolicy

import com.ferdidrgn.theatreticket.util.Response

interface GetTermsConditionOrPrivacyPolicyUseCase {
    operator fun invoke(whichTermsAndPrivacy: String, status: (Response, String?) -> Unit): Unit
}