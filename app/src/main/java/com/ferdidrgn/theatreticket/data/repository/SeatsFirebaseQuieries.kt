package com.ferdidrgn.theatreticket.data.repository

import com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData.Seats
import com.ferdidrgn.theatreticket.util.Response
import com.google.firebase.Timestamp
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SeatsFirebaseQuieries {
    private val fireStoreSeatsRef = Firebase.firestore.collection("Seats")

    fun checkIsEmptySeat(seatsId: String, status: (Boolean, Boolean?) -> Unit) {

        fireStoreSeatsRef.whereEqualTo("_id", seatsId).get()
            .addOnSuccessListener { result ->

                if (result == null || result.isEmpty)
                    status.invoke(false, null)
                else {
                    var isEmpty = true
                    val documents = result.documents
                    for (document in documents) {
                        isEmpty =
                            if (document.get("isEmpty") != null) document.get("isEmpty") as Boolean else true
                    }
                    status.invoke(true, isEmpty)
                }
            }
    }

    fun updateSeats(seats: Seats?, status: (Boolean) -> Unit) {

        var documentId = ""
        fireStoreSeatsRef.whereEqualTo("_id", seats?._id).get()
            .addOnSuccessListener { result ->
                if (result != null) {
                    val documents = result.documents
                    for (document in documents) {
                        documentId = document.id
                    }

                    val seatMap = putHashMap(seats, true)
                    fireStoreSeatsRef.document(documentId)
                        .update(seatMap)
                        .addOnSuccessListener {
                            status.invoke(true)
                        }.addOnFailureListener {
                            status.invoke(false)
                        }
                }
            }
    }

    fun getSeatId(seatId: String?, status: (Response, List<Seats?>?) -> Unit) {
        var seatList: ArrayList<Seats?> = arrayListOf()
        fireStoreSeatsRef.whereEqualTo("seatId", seatId).get()
            .addOnSuccessListener { result ->

                if (result == null || result.isEmpty)
                    status.invoke(Response.ServerError, null)
                else {
                    seatList = getAllHashMap(result)
                    status.invoke(Response.ThereIs, seatList)
                }
            }
    }


    private fun putHashMap(
        seats: Seats?,
        isUpdate: Boolean
    ): HashMap<String, Any> {
        val seatMap = HashMap<String, Any>()
        if (!isUpdate)
            seatMap["_createdAt"] = Timestamp.now()

        seatMap["_id"] = seats?._id.toString()
        seatMap["name"] = seats?.name.toString()
        seatMap["isEmpty"] = seats?.isEmpty.toString().toBoolean()
        seatMap["seatId"] = seats?.seatId.toString()

        return seatMap
    }

    private fun getAllHashMap(result: QuerySnapshot): ArrayList<Seats?> {
        val seatsList: ArrayList<Seats?> = arrayListOf()
        val documents = result.documents
        for (document in documents) {
            val createdAt =
                if (document.get("_createdAt") != null) document.get("_createdAt") as Timestamp else ""
            val id =
                if (document.get("_id") != null) document.get("_id") as String else ""
            val name =
                if (document.get("name") != null) document.get("name") as String else ""
            val isEmpty =
                if (document.get("isEmpty") != null) document.get("isEmpty") as Boolean else true
            val seatId =
                if (document.get("seatId") != null) document.get("seatId") as String else ""


            val seats = Seats(
                _createdAt = createdAt.toString(),
                _id = id,
                name = name,
                isEmpty = isEmpty,
                seatId = seatId
            )
            seatsList?.add(seats)
        }
        return seatsList
    }

}