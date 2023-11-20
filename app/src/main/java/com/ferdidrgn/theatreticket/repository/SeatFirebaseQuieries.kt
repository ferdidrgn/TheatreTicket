package com.ferdidrgn.theatreticket.repository

import com.ferdidrgn.theatreticket.commonModels.dummyData.Seat
import com.ferdidrgn.theatreticket.enums.Response
import com.google.firebase.Timestamp
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SeatFirebaseQuieries {
    private val fireStoreSeatRef = Firebase.firestore.collection("Seat")

    fun getSeatId(seatId: String?, status: (Response, ArrayList<Seat?>?) -> Unit) {

        var seatList: ArrayList<Seat?> = arrayListOf()
        fireStoreSeatRef.whereEqualTo("_id", seatId).get()
            .addOnSuccessListener { result ->

                if (result == null || result.isEmpty)
                    status.invoke(Response.ServerError, null)
                else {
                    seatList = getAllHashMap(result)
                    status.invoke(Response.ThereIs, seatList)
                }
            }
    }

    fun checkIsSelectedSeat(seatId: String, status: (Boolean, Boolean?) -> Unit) {

        fireStoreSeatRef.whereEqualTo("_id", seatId).get()
            .addOnSuccessListener { result ->

                if (result == null || result.isEmpty)
                    status.invoke(false, null)
                else {
                    var isSelected = true
                    val documents = result.documents
                    for (document in documents) {
                        isSelected =
                            if (document.get("isSelected") != null) document.get("isSelected") as Boolean else true
                    }
                    status.invoke(true, isSelected)
                }
            }
    }

    fun updateSeat(seat: Seat?, status: (Boolean) -> Unit) {

        var documentId = ""
        fireStoreSeatRef.whereEqualTo("_id", seat?._id).get()
            .addOnSuccessListener { result ->
                if (result != null) {
                    val documents = result.documents
                    for (document in documents) {
                        documentId = document.id
                    }

                    val seatMap = putHashMap(seat, true)
                    fireStoreSeatRef.document(documentId)
                        .update(seatMap)
                        .addOnSuccessListener {
                            status.invoke(true)
                        }.addOnFailureListener {
                            status.invoke(false)
                        }
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
        seatMap["name"] = seat?.name.toString()
        seatMap["isSelected"] = seat?.isSelected.toString()
        seatMap["stageId"] = seat?.stageId.toString()

        return seatMap
    }


    private fun getAllHashMap(result: QuerySnapshot): ArrayList<Seat?> {
        val seatList: ArrayList<Seat?> = arrayListOf()
        val documents = result.documents
        for (document in documents) {
            val createdAt =
                if (document.get("_createdAt") != null) document.get("_createdAt") as Timestamp else ""
            val id =
                if (document.get("_id") != null) document.get("_id") as String else ""
            val isSelected =
                if (document.get("isSelected") != null) document.get("isSelected") as Boolean else true
            val stageId =
                if (document.get("stageId") != null) document.get("stageId") as String else ""
            val name =
                if (document.get("name") != null) document.get("name") as String else ""

            val seat = Seat(
                _createdAt = createdAt.toString(),
                _id = id,
                name = name,
                isSelected = isSelected,
                stageId = stageId
            )
            seatList?.add(seat)
        }
        return seatList
    }
}