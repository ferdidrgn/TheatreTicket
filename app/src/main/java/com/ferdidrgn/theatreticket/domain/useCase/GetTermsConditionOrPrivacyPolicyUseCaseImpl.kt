package com.ferdidrgn.theatreticket.domain.useCase

import com.ferdidrgn.theatreticket.data.repository.AppToolsFireBaseQueriesRepository
import com.ferdidrgn.theatreticket.tools.enums.Response
import javax.inject.Inject

class GetTermsConditionOrPrivacyPolicyUseCaseImpl @Inject constructor(
    private val appToolsFireBaseQueriesRepository: AppToolsFireBaseQueriesRepository,
) : GetTermsConditionOrPrivacyPolicyUseCase {

    override operator fun invoke(
        whichTermsAndPrivacy: String,
        status: (Response, String?) -> Unit
    ): Unit =
        appToolsFireBaseQueriesRepository.getTermsConditionOrPrivacyPolicy(
            whichTermsAndPrivacy, status
        )
}