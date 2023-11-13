package com.ferdidrgn.theatreticket.repository

import com.ferdidrgn.theatreticket.commonModels.dummyData.Stage
import com.ferdidrgn.theatreticket.enums.Response
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class StageFirebaseQueries {

    private val fireStoreStageRef = Firebase.firestore.collection("Stage")
    var storageRef = Firebase.storage.reference

    fun addStage(stage: Stage?, status: (Boolean) -> Unit) {
        var downloadUrl = ""
        val imageName = "StageImages/${stage?._id}.jpg"
        val imagesRef = storageRef.child(imageName)

        if (stage?.addOrUpdateImgUrl != null) {
            imagesRef.putFile(stage.addOrUpdateImgUrl!!).addOnSuccessListener {
                Firebase.storage.reference.child(imageName).downloadUrl.addOnSuccessListener { uri ->
                    downloadUrl = uri.toString()
                }
            }
        }

        val stageMap = HashMap<String, Any>()
        stageMap["_createdAt"] = Timestamp.now()
        stageMap["_id"] = stage?._id.toString()
        stageMap["name"] = stage?.name.toString()
        downloadUrl = if (downloadUrl == "") stage?.imgUrl.toString() else downloadUrl
        stageMap["imgUrl"] = downloadUrl
        stageMap["capacity"] = stage?.capacity.toString()
        stageMap["description"] = stage?.description.toString()
        stageMap["communication"] = stage?.communication.toString()
        stageMap["address"] = stage?.address.toString()
        stageMap["locationLat"] = stage?.locationLat.toString()
        stageMap["locationLng"] = stage?.locationLng.toString()
        stageMap["seatColumnCount"] = stage?.seatColumnCount.toString()
        stageMap["seatRowCount"] = stage?.seatRowCount.toString()

        fireStoreStageRef.add(stageMap).addOnCompleteListener { task ->
            if (task.isComplete && task.isSuccessful) {
                status.invoke(true)
            } else {
                status.invoke(false)
            }
        }.addOnFailureListener { status.invoke(false) }
    }

    fun deleteStage(show: Stage?, status: (Boolean) -> Unit) =
        CoroutineScope(Dispatchers.IO).launch {
            val shoQuery = fireStoreStageRef
                .whereEqualTo("_id", show?._id)
                .whereEqualTo("name", show?.name)
                .get()
                .await()
            if (shoQuery.documents.isNotEmpty()) {
                for (document in shoQuery) {
                    try {
                        fireStoreStageRef.document(document.id).delete().await()
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

    fun getStage(status: (Response, ArrayList<Stage?>?) -> Unit) {

        val stageList: ArrayList<Stage?> = arrayListOf()
        fireStoreStageRef.orderBy("_createdAt", Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) status.invoke(Response.ServerError, null)
                else {
                    if (value != null) {
                        if (!value.isEmpty) {
                            val documents = value.documents
                            for (document in documents) {
                                val createdAt =
                                    if (document.get("_createdAt") != null) document.get("_createdAt") as Timestamp else ""
                                val id =
                                    if (document.get("_id") != null) document.get("_id") as String else ""
                                val imgUrl =
                                    if (document.get("imgUrl") != null) document.get("imgUrl") as String else ""
                                val name =
                                    if (document.get("name") != null) document.get("name") as String else ""
                                val description =
                                    if (document.get("description") != null) document.get("description") as String else ""
                                val capacity =
                                    if (document.get("capacity") != null) document.get("capacity") as String else ""
                                val communication =
                                    if (document.get("communication") != null) document.get("communication") as String else ""
                                val address =
                                    if (document.get("address") != null) document.get("address") as String else ""
                                val locationLat =
                                    if (document.get("locationLat") != null) document.get("locationLat") as String else "0.0"
                                val locationLng =
                                    if (document.get("locationLng") != null) document.get("locationLng") as String else "0.0"
                                val seatColumnCount =
                                    if (document.get("seatColumnCount") != null) document.get("seatColumnCount") as String else "0"
                                val seatRowCount =
                                    if (document.get("seatRowCount") != null) document.get("seatRowCount") as String else "0"

                                val stage = Stage(
                                    _createdAt = createdAt.toString(),
                                    _id = id,
                                    imgUrl = imgUrl,
                                    name = name,
                                    description = description,
                                    capacity = capacity,
                                    communication = communication,
                                    address = address,
                                )
                                stageList.add(stage)
                            }
                            status.invoke(Response.ThereIs, stageList)
                        } else {
                            status.invoke(Response.Empty, null)
                        }
                    } else {
                        status.invoke(Response.Empty, null)
                    }
                }
            }
    }

    fun getStageId(stageId: ArrayList<String?>?, status: (Response, ArrayList<Stage?>?) -> Unit) {
        val stageList: ArrayList<Stage?> = arrayListOf()

        stageId?.forEach { stage ->
            fireStoreStageRef.whereEqualTo("_id", stage).get()
                .addOnSuccessListener { result ->
                    if (result.isEmpty) {
                        status.invoke(Response.Empty, null)
                    } else {
                        if (result != null) {
                            val documents = result.documents
                            for (document in documents) {
                                val createdAt =
                                    if (document.get("_createdAt") != null) document.get("_createdAt") as Timestamp else ""
                                val id =
                                    if (document.get("_id") != null) document.get("_id") as String else ""
                                val imgUrl =
                                    if (document.get("imgUrl") != null) document.get("imgUrl") as String else ""
                                val name =
                                    if (document.get("name") != null) document.get("name") as String else ""
                                val description =
                                    if (document.get("description") != null) document.get("description") as String else ""
                                val capacity =
                                    if (document.get("capacity") != null) document.get("capacity") as String else ""
                                val communication =
                                    if (document.get("communication") != null) document.get("communication") as String else ""
                                val address =
                                    if (document.get("address") != null) document.get("address") as String else ""
                                val locationLat =
                                    if (document.get("locationLat") != null) document.get("locationLat") as Double else 0.0
                                val locationLng =
                                    if (document.get("locationLng") != null) document.get("locationLng") as Double else 0.0
                                val seatColumnCount =
                                    if (document.get("seatColumnCount") != null) document.get("seatColumnCount") as Int else 0
                                val seatRowCount =
                                    if (document.get("seatRowCount") != null) document.get("seatRowCount") as Int else 0

                                val stages = Stage(
                                    _createdAt = createdAt.toString(),
                                    _id = id,
                                    imgUrl = imgUrl,
                                    name = name,
                                    description = description,
                                    capacity = capacity,
                                    communication = communication,
                                    address = address,
                                    locationLat = locationLat,
                                    locationLng = locationLng,
                                    seatColumnCount = seatColumnCount,
                                    seatRowCount = seatRowCount
                                )
                                stageList.add(stages)
                            }
                            status.invoke(Response.ThereIs, stageList)
                        } else {
                            status.invoke(Response.Empty, null)
                        }
                    }
                }.addOnFailureListener {
                    status.invoke(Response.ServerError, null)
                }
        }
    }

    fun updateStage(stage: Stage?, status: (Boolean) -> Unit) {

        val imageName = "StageImages/${stage?._id}.jpg"
        val imagesRef = storageRef.child(imageName)
        var downloadUrl = ""
        var documentId = ""
        fireStoreStageRef.whereEqualTo("_id", stage?._id).get()
            .addOnSuccessListener { result ->
                if (result != null) {
                    val documents = result.documents
                    for (document in documents) {
                        documentId = document.id
                    }
                }
            }.addOnFailureListener { status.invoke(false) }

        if (stage?.addOrUpdateImgUrl != null) {
            imagesRef.putFile(stage?.addOrUpdateImgUrl!!).addOnSuccessListener {
                Firebase.storage.reference.child(imageName).downloadUrl.addOnSuccessListener { uri ->
                    downloadUrl = uri.toString()
                }.addOnFailureListener { status.invoke(false) }
            }.addOnFailureListener { status.invoke(false) }
        }

        downloadUrl = if (downloadUrl == "") stage?.imgUrl.toString() else downloadUrl

        val newShowMap = mapOf(
            "_id" to stage?._id.toString(),
            "name" to stage?.name.toString(),
            "imgUrl" to downloadUrl,
            "capacity" to stage?.capacity.toString(),
            "description" to stage?.description.toString(),
            "communication" to stage?.communication.toString(),
            "address" to stage?.address.toString(),
            "locationLat" to stage?.locationLat.toString(),
            "locationLng" to stage?.locationLng.toString(),
            "seatId" to stage?.seatId.toString(),
            "seatColumnCount" to stage?.seatColumnCount.toString(),
            "seatRowCount" to stage?.seatRowCount.toString()

        )
        fireStoreStageRef.document(documentId).update(newShowMap)
            .addOnSuccessListener {
                status.invoke(true)
            }.addOnFailureListener {
                status.invoke(false)
            }
    }
}