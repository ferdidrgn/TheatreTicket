package com.ferdidrgn.theatreticket.repository

import com.ferdidrgn.theatreticket.commonModels.dummyData.Show
import com.ferdidrgn.theatreticket.enums.Response
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ShowFirebaseQuieries {

    private val fireStore = Firebase.firestore

    fun addShow(show: Show, status: (Boolean) -> Unit) {
        val showMap = HashMap<String, Any>()
        showMap["_createdAt"] = Timestamp.now()
        showMap["_id"] = show._id.toString()
        showMap["name"] = show.name.toString()
        showMap["description"] = show.description.toString()
        showMap["date"] = show.date.toString()
        showMap["price"] = show.price.toString()
        showMap["ageLimit"] = show.ageLimit.toString()
        showMap["players"] = show.actorsId.toString()

        fireStore.collection("Show").add(showMap).addOnSuccessListener {
            status.invoke(true)
        }.addOnFailureListener {
            status.invoke(false)
        }
    }

    fun deleteShow(show: Show?, status: (Boolean) -> Unit) {
        fireStore.collection("Show").document(show?._id.toString()).delete()
            .addOnSuccessListener {
                status.invoke(true)
            }.addOnFailureListener {
                status.invoke(false)
            }
    }

    fun getShow(status: (String, ArrayList<Show?>?) -> Unit) {
        var statusTree = ""
        val showList: ArrayList<Show?> = arrayListOf()
        fireStore.collection("Show").orderBy("name", Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    statusTree = Response.ServerError.response
                    status.invoke(statusTree, null)
                } else {
                    if (value != null) {
                        if (!value.isEmpty) {
                            val documents = value.documents
                            for (document in documents) {
                                val createdAt =
                                    if (document.get("_createdAt") != null) document.get("_createdAt") as Timestamp else ""
                                val id =
                                    if (document.get("_id") != null) document.get("_id") as String else ""
                                val name =
                                    if (document.get("name") != null) document.get("name") as String else ""
                                val description =
                                    if (document.get("description") != null) document.get("description") as String else ""
                                val date =
                                    if (document.get("date") != null) document.get("date") as String else ""
                                val price =
                                    if (document.get("price") != null) document.get("price") as String else ""
                                val ageLimit =
                                    if (document.get("ageLimit") != null) document.get("ageLimit") as String else ""

                                val show = Show(
                                    _createdAt = createdAt.toString(),
                                    _id = id,
                                    name = name,
                                    description = description,
                                    date = date,
                                    price = price,
                                    ageLimit = ageLimit,
                                )
                                showList?.add(show)
                            }
                            statusTree = Response.ThereIs.response
                            status.invoke(statusTree, showList)
                        } else {
                            statusTree = Response.Empty.response
                            status.invoke(statusTree, null)
                        }
                    } else {
                        statusTree = Response.Empty.response
                        status.invoke(statusTree, null)
                    }
                }
            }
    }
}