package com.ferdidrgn.theatreticket.domain.useCase.contactUsEmail

import com.ferdidrgn.theatreticket.domain.repository.AppToolsFireBaseQueriesRepository
import com.ferdidrgn.theatreticket.util.Response
import javax.inject.Inject

class GetContactUsEmailUseCaseImpl @Inject constructor(
    private val appToolsFireBaseQueriesRepository: AppToolsFireBaseQueriesRepository,
) : GetContactUsEmailUseCase {
    override fun invoke(status: (Response, String?) -> Unit): Unit =
        appToolsFireBaseQueriesRepository.getContactUsEmail(status)
}