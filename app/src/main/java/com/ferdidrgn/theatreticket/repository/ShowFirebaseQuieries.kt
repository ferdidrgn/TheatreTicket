package com.ferdidrgn.theatreticket.repository

import com.ferdidrgn.theatreticket.commonModels.dummyData.Show
import com.ferdidrgn.theatreticket.enums.Response
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ShowFirebaseQuieries {

    private val fireStoreShowRef = Firebase.firestore.collection("Show")
    var storageRef = Firebase.storage.reference

    fun addShow(show: Show?, status: (Boolean) -> Unit) {
        val imageName = "ShowImages/${show?._id}.jpg"
        val imagesRef = storageRef.child(imageName)
        if (show?.imageUrl != null) {
            imagesRef.putFile(show.imageUrl!!).addOnSuccessListener {
                Firebase.storage.reference.child(imageName).downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    val showMap = HashMap<String, Any>()
                    showMap["_createdAt"] = Timestamp.now()
                    showMap["_id"] = show._id.toString()
                    showMap["name"] = show.name.toString()
                    showMap["imageUrl"] = downloadUrl
                    showMap["description"] = show.description.toString()
                    showMap["date"] = show.date.toString()
                    showMap["price"] = show.price.toString()
                    showMap["ageLimit"] = show.ageLimit.toString()
                    showMap["players"] = show.actorsId.toString()

                    fireStoreShowRef.add(showMap).addOnCompleteListener { task ->
                        if (task.isComplete && task.isSuccessful) {
                            status.invoke(true)
                        } else {
                            status.invoke(false)
                        }
                    }.addOnFailureListener { status.invoke(false) }
                }.addOnFailureListener { status.invoke(false) }
            }.addOnFailureListener { status.invoke(false) }
        } else status.invoke(false)
    }

    fun deleteShow(show: Show?, status: (Boolean) -> Unit) = CoroutineScope(Dispatchers.IO).launch {
        val shoQuery = fireStoreShowRef
            .whereEqualTo("_id", show?._id)
            .whereEqualTo("name", show?.name)
            .get()
            .await()
        if (shoQuery.documents.isNotEmpty()) {
            for (document in shoQuery) {
                try {
                    fireStoreShowRef.document(document.id).delete().await()
                    /*personCollectionRef.document(document.id).update(mapOf(
                        "firstName" to FieldValue.delete()
                    )).await()*/
                    status.invoke(true)
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        status.invoke(false)
                    }
                }
            }
        } else {
            withContext(Dispatchers.Main) {
                status.invoke(false)
            }
        }
    }

    fun getShow(status: (String, ArrayList<Show?>?) -> Unit) {

        var statusTree = ""
        val showList: ArrayList<Show?> = arrayListOf()
        fireStoreShowRef.orderBy("name", Query.Direction.ASCENDING)
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

    fun updateShow(show: Show?, status: (Boolean) -> Unit) {
        val newShowMap = mapOf(
            "_createdAt" to Timestamp.now(),
            "name" to show?.name.toString(),
            "description" to show?.description.toString(),
            "date" to show?.date.toString(),
            "price" to show?.price.toString(),
            "ageLimit" to show?.ageLimit.toString()
        )

        fireStoreShowRef.whereEqualTo("name", show?.name)
            .whereEqualTo("_id", show?._id)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    fireStoreShowRef.document(document.id).set(newShowMap, SetOptions.merge())
                        .addOnSuccessListener {
                            status.invoke(true)
                        }.addOnFailureListener {
                            status.invoke(false)
                        }
                }
            }
    }
}