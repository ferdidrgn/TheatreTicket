package com.ferdidrgn.theatreticket.domain.useCase.contactUsEmail

import com.ferdidrgn.theatreticket.util.Response

interface GetContactUsEmailUseCase {
    operator fun invoke(status: (Response, String?) -> Unit): Unit
}