package com.ferdidrgn.theatreticket.domain.repository

import com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData.Seat
import com.ferdidrgn.theatreticket.util.Response
import com.google.firebase.firestore.QuerySnapshot

interface SeatFirebaseQueriesRepository {

    fun getSeatId(seatId: String?, status: (Response, Seat?) -> Unit ): Unit

    fun putHashMap(
        seat: Seat?,
        isUpdate: Boolean
    ): HashMap<String, Any>

    fun getAllHashMap(result: QuerySnapshot): Seat?

}