package com.ferdidrgn.theatreticket.data.repository

import com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData.Seat
import com.ferdidrgn.theatreticket.tools.enums.Response
import com.google.firebase.Timestamp
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SeatFirebaseQuieries {
    private val fireStoreSeatRef = Firebase.firestore.collection("Seat")

    fun getSeatId(seatId: String?, status: (Response, Seat?) -> Unit) {

        var seat: Seat? = null
        fireStoreSeatRef.whereEqualTo("_id", seatId).get()
            .addOnSuccessListener { result ->

                if (result == null || result.isEmpty)
                    status.invoke(Response.ServerError, null)
                else {
                    seat = getAllHashMap(result)
                    status.invoke(Response.ThereIs, seat)
                }
            }
    }

    private fun putHashMap(
        seat: Seat?,
        isUpdate: Boolean
    ): HashMap<String, Any> {
        val seatMap = HashMap<String, Any>()
        if (!isUpdate)
            seatMap["_createdAt"] = Timestamp.now()

        seatMap["_id"] = seat?._id.toString()
        seatMap["stageId"] = seat?.stageId.toString()

        return seatMap
    }


    private fun getAllHashMap(result: QuerySnapshot): Seat? {
        var seat: Seat? = null
        val documents = result.documents
        for (document in documents) {
            val createdAt =
                if (document.get("_createdAt") != null) document.get("_createdAt") as Timestamp else ""
            val id =
                if (document.get("_id") != null) document.get("_id") as String else ""
            val stageId =
                if (document.get("stageId") != null) document.get("stageId") as String else ""

            seat = Seat(
                _createdAt = createdAt.toString(),
                _id = id,
                stageId = stageId
            )
        }
        return seat
    }
}