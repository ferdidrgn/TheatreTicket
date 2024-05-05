package com.ferdidrgn.theatreticket.domain.useCase

import com.ferdidrgn.theatreticket.tools.enums.Response

interface GetTermsConditionOrPrivacyPolicyUseCase {
    operator fun invoke(whichTermsAndPrivacy: String, status: (Response, String?) -> Unit): Unit
}