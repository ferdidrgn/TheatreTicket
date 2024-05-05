package com.ferdidrgn.theatreticket.domain.useCase

import com.ferdidrgn.theatreticket.data.repository.AppToolsFireBaseQueriesRepository
import com.ferdidrgn.theatreticket.tools.enums.Response
import javax.inject.Inject

class GetContactUsEmailUseCaseImpl @Inject constructor(
    private val appToolsFireBaseQueriesRepository: AppToolsFireBaseQueriesRepository,
) : GetContactUsEmailUseCase {
    override fun invoke(status: (Response, String?) -> Unit): Unit =
        appToolsFireBaseQueriesRepository.getContactUsEmail(status)
}