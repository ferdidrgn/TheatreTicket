package com.ferdidrgn.theatreticket.domain.useCase

import com.ferdidrgn.theatreticket.tools.enums.Response

interface GetContactUsEmailUseCase {
    operator fun invoke(status: (Response, String?) -> Unit): Unit
}