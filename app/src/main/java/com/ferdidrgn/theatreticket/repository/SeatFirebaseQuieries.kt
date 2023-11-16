package com.ferdidrgn.theatreticket.repository

import com.ferdidrgn.theatreticket.commonModels.dummyData.Seat
import com.ferdidrgn.theatreticket.commonModels.dummyData.Show
import com.ferdidrgn.theatreticket.enums.Response
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SeatFirebaseQuieries {
    private val fireStoreShowRef = Firebase.firestore.collection("Stage")

    fun getSeat(status: (Response, ArrayList<Seat?>?) -> Unit) {

        var seatList: ArrayList<Seat?> = arrayListOf()
        fireStoreShowRef.orderBy("_createdAt", Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) status.invoke(Response.ServerError, null)
                else {
                    if (value == null || value.isEmpty) {
                        status.invoke(Response.Empty, null)
                    } else {
                        seatList = getAllHashMap(value)
                        status.invoke(Response.ThereIs, seatList)
                    }
                }
            }
    }

    fun updateSeat(seat: Seat?, status: (Boolean) -> Unit) {

        var documentId = ""
        fireStoreShowRef.whereEqualTo("_id", seat?._id).get()
            .addOnSuccessListener { result ->
                if (result != null) {
                    val documents = result.documents
                    for (document in documents) {
                        documentId = document.id
                    }

                    val seatMap = putHashMap(seat, true)
                    fireStoreShowRef.document(documentId).update(seatMap)
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

        val seatIdList = ArrayList<String>()
        seat?.name?.forEach { element ->
            seatIdList.add(element.toString())

        }
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
            val name = if (document.get("name") != null) document.get("name") as String else ""

            val seat = Seat(
                _createdAt = createdAt.toString(),
                _id = id,
                name = name,
                isSelected = isSelected,
            )
            seatList?.add(seat)
        }
        return seatList
    }
}