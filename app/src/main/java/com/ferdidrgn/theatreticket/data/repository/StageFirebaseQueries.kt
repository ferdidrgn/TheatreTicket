package com.ferdidrgn.theatreticket.data.repository

import android.net.Uri
import com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData.Stage
import com.ferdidrgn.theatreticket.tools.enums.Response
import com.ferdidrgn.theatreticket.tools.showToast
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
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

        val downloadUrl = putStrogeImage(
            stage?._id.toString(),
            stage?.addOrUpdateImgUrl,
            stage?.imgUrl.toString()
        )

        val stageMap = putHashMap(stage, downloadUrl, false)

        fireStoreStageRef.add(stageMap).addOnCompleteListener { task ->
            if (task.isComplete && task.isSuccessful) {
                status.invoke(true)
            } else {
                status.invoke(false)
            }
        }.addOnFailureListener { status.invoke(false) }
    }

    fun deleteStage(stage: Stage?, status: (Boolean) -> Unit) =
        CoroutineScope(Dispatchers.IO).launch {

            val shoQuery = fireStoreStageRef
                .whereEqualTo("_id", stage?._id)
                .whereEqualTo("name", stage?.name)
                .get()
                .await()
            if (shoQuery.documents.isNotEmpty()) {
                for (document in shoQuery) {
                    try {
                        fireStoreStageRef.document(document.id).delete().await()
                        /*personCollectionRef.document(document.id).update(mapOf(
                            "firstName" to FieldValue.delete()
                        )).await()*/
                        deleteStrogeImage(stage?._id.toString())
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

        var stageList: ArrayList<Stage?> = arrayListOf()
        fireStoreStageRef.orderBy("_createdAt", Query.Direction.ASCENDING)
            .addSnapshotListener { result, error ->
                if (error != null) status.invoke(Response.ServerError, null)
                else {
                    if (result == null || result.isEmpty) {
                        status.invoke(Response.Empty, null)
                    } else {
                        stageList = getAllHashMap(result)
                        status.invoke(Response.ThereIs, stageList)
                    }
                }
            }
    }

    fun getStageId(stageId: ArrayList<Any>?, status: (Response, ArrayList<Stage?>?) -> Unit) {
        var stageList: ArrayList<Stage?>? = null
        val firstSageId = stageId?.first()

        fireStoreStageRef.whereEqualTo("_id", firstSageId).get()
            .addOnSuccessListener { result ->
                if (result.isEmpty || result == null) {
                    status.invoke(Response.Empty, null)
                } else {
                    stageList = getAllHashMap(result)
                    status.invoke(Response.ThereIs, stageList)
                }
            }.addOnFailureListener { status.invoke(Response.ServerError, null) }
    }

    fun updateStage(stage: Stage?, status: (Boolean) -> Unit) {

        var downloadUrl = putStrogeImage(
            stage?._id.toString(),
            stage?.addOrUpdateImgUrl,
            stage?.imgUrl.toString()
        )

        var documentId = ""
        fireStoreStageRef.whereEqualTo("_id", stage?._id).get()
            .addOnSuccessListener { result ->
                if (result != null) {
                    val documents = result.documents
                    for (document in documents) {
                        documentId = document.id
                    }

                    downloadUrl = if (downloadUrl == "") stage?.imgUrl.toString() else downloadUrl

                    val stageMap = putHashMap(stage, downloadUrl, true)
                    fireStoreStageRef.document(documentId).update(stageMap)
                        .addOnSuccessListener {
                            status.invoke(true)
                        }.addOnFailureListener {
                            status.invoke(false)
                        }
                }
            }
    }

    private fun getDocumandId(stageId: String): String {
        var documentId = ""
        fireStoreStageRef.whereEqualTo("_id", stageId).get()
            .addOnSuccessListener { result ->
                if (result != null) {
                    val documents = result.documents
                    for (document in documents) {
                        documentId = document.id
                    }
                }
            }
        return documentId
    }

    private fun putStrogeImage(
        stageId: String,
        stageAddOrUpdateImgUrl: Uri?,
        stageImgUrl: String
    ): String {
        val imageName = "StageImages/${stageId}.jpg"
        val imagesRef = storageRef.child(imageName)
        var downloadUrl = ""
        if (stageAddOrUpdateImgUrl != null) {
            imagesRef.putFile(stageAddOrUpdateImgUrl).addOnSuccessListener {
                Firebase.storage.reference.child(imageName).downloadUrl.addOnSuccessListener { uri ->
                    downloadUrl = uri.toString()
                }
            }
        }
        downloadUrl = if (downloadUrl == "") stageImgUrl else downloadUrl
        return downloadUrl
    }

    private fun deleteStrogeImage(stageId: String) {
        val imageName = "StageImages/${stageId}.jpg"
        val imagesRef = storageRef.child(imageName)
        imagesRef.delete().addOnSuccessListener {
            showToast("Resim silindi")
        }.addOnFailureListener {
            showToast("Resim silinemedi")
        }
    }

    private fun putHashMap(
        stage: Stage?,
        downloadUrl: String,
        isUpdate: Boolean
    ): HashMap<String, Any> {
        val stageMap = HashMap<String, Any>()
        if (!isUpdate)
            stageMap["_createdAt"] = Timestamp.now()

        stageMap["_id"] = stage?._id.toString()
        stageMap["name"] = stage?.name.toString()
        stageMap["imgUrl"] = downloadUrl
        stageMap["capacity"] = stage?.capacity.toString()
        stageMap["description"] = stage?.description.toString()
        stageMap["communication"] = stage?.communication.toString()
        stageMap["address"] = stage?.address.toString()
        stageMap["locationLat"] = stage?.locationLat.toString()
        stageMap["locationLng"] = stage?.locationLng.toString()
        stageMap["seatColumnCount"] = stage?.seatColumnCount.toString()
        stageMap["seatRowCount"] = stage?.seatRowCount.toString()
        stageMap["seatId"] = stage?.seatId.toString()
        return stageMap
    }

    private fun getAllHashMap(result: QuerySnapshot): ArrayList<Stage?> {
        val stageList: ArrayList<Stage?> = arrayListOf()
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
                if (document.get("locationLat") != null) document.get("locationLat") as String else 0.0
            val locationLng =
                if (document.get("locationLng") != null) document.get("locationLng") as String else 0.0
            val seatColumnCount =
                if (document.get("seatColumnCount") != null) document.get("seatColumnCount") as String else 0
            val seatRowCount =
                if (document.get("seatRowCount") != null) document.get("seatRowCount") as String else 0
            val seatId =
                if (document.get("seatId") != null) document.get("seatId") as String else ""

            val stages = Stage(
                _createdAt = createdAt.toString(),
                _id = id,
                imgUrl = imgUrl,
                name = name,
                description = description,
                capacity = capacity,
                communication = communication,
                address = address,
                locationLat = locationLat?.toString()?.toDoubleOrNull() ?: 0.0,
                locationLng = locationLng?.toString()?.toDoubleOrNull() ?: 0.0,
                seatId = seatId,
                //seatColumnCount = seatColumnCount,
                //seatRowCount = seatRowCount
            )
            stageList.add(stages)
        }
        return stageList
    }
}