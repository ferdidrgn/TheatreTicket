package com.ferdidrgn.theatreticket.domain.useCase.termsConditionPrivacyPolicy

import com.ferdidrgn.theatreticket.domain.repository.AppToolsFireBaseQueriesRepository
import com.ferdidrgn.theatreticket.util.Response
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