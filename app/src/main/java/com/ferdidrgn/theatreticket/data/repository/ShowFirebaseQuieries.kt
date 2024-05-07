package com.ferdidrgn.theatreticket.data.repository

import android.net.Uri
import com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData.Show
import com.ferdidrgn.theatreticket.util.Response
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
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ShowFirebaseQuieries {

    private val fireStoreShowRef = Firebase.firestore.collection("Show")
    private var storageRef = Firebase.storage.reference

    fun addShow(show: Show?, status: (Boolean) -> Unit) {

        val downloadUrl = putStrogeImage(
            show?._id.toString(),
            show?.addOrUpdateImgUrl,
            show?.imageUrl.toString()
        )

        val stageMap = putHashMap(show, downloadUrl, false)

        fireStoreShowRef.add(stageMap).addOnCompleteListener { task ->
            if (task.isComplete && task.isSuccessful) {
                status.invoke(true)
            } else {
                status.invoke(false)
            }
        }.addOnFailureListener { status.invoke(false) }
    }

    fun deleteShow(show: Show?, status: (Boolean) -> Unit) = CoroutineScope(Dispatchers.IO).launch {
        val showQuery = fireStoreShowRef
            .whereEqualTo("_id", show?._id)
            .whereEqualTo("name", show?.name)
            .get()
            .await()
        if (showQuery.documents.isNotEmpty()) {
            for (document in showQuery) {
                try {
                    fireStoreShowRef.document(document.id).delete().await()
                    /*personCollectionRef.document(document.id).update(mapOf(
                        "firstName" to FieldValue.delete()
                    )).await()*/
                    deleteStrogeImage(show?._id.toString())
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

    fun getShow(status: (Response, ArrayList<Show?>?) -> Unit) {

        var showList: ArrayList<Show?> = arrayListOf()
        fireStoreShowRef.orderBy("_createdAt", Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) status.invoke(Response.ServerError, null)
                else {
                    if (value == null || value.isEmpty) {
                        status.invoke(Response.Empty, null)
                    } else {
                        showList = getAllHashMap(value)
                        status.invoke(Response.ThereIs, showList)
                    }
                }
            }
    }

    fun updateShow(show: Show?, status: (Boolean) -> Unit) {

        var downloadUrl = putStrogeImage(
            show?._id.toString(),
            show?.addOrUpdateImgUrl,
            show?.imageUrl.toString()
        )

        var documentId = ""
        fireStoreShowRef.whereEqualTo("_id", show?._id).get()
            .addOnSuccessListener { result ->
                if (result != null) {
                    val documents = result.documents
                    for (document in documents) {
                        documentId = document.id
                    }

                    downloadUrl = if (downloadUrl == "") show?.imageUrl.toString() else downloadUrl

                    val showMap = putHashMap(show, downloadUrl, true)
                    fireStoreShowRef.document(documentId).update(showMap)
                        .addOnSuccessListener {
                            status.invoke(true)
                        }.addOnFailureListener {
                            status.invoke(false)
                        }
                }
            }
    }

    fun getShowId(showId: String, status: (Response, Show?) -> Unit) {

        var showList: ArrayList<Show?> = arrayListOf()

        fireStoreShowRef.whereEqualTo("_id", showId).get()
            .addOnSuccessListener { result ->
                if (result.isEmpty || result == null) {
                    status.invoke(Response.Empty, null)
                } else {
                    showList = getAllHashMap(result)
                    status.invoke(Response.ThereIs, showList.first())
                }
            }.addOnFailureListener {
                status.invoke(Response.ServerError, null)
            }
    }

    private fun getDocumandId(showId: String): String {
        var documentId = ""
        fireStoreShowRef.whereEqualTo("_id", showId).get()
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
        showId: String,
        showIdAddOrUpdateImgUrl: Uri?,
        showIdImgUrl: String
    ): String {
        val imageName = "ShowImages/${showId}.jpg"
        val imagesRef = storageRef.child(imageName)
        var downloadUrl = ""
        if (showIdAddOrUpdateImgUrl != null) {
            imagesRef.putFile(showIdAddOrUpdateImgUrl).addOnSuccessListener {
                Firebase.storage.reference.child(imageName).downloadUrl.addOnSuccessListener { uri ->
                    downloadUrl = uri.toString()
                }
            }
        }
        downloadUrl = if (downloadUrl == "") showIdImgUrl else downloadUrl
        return downloadUrl
    }

    private fun deleteStrogeImage(stageId: String) {
        val imageName = "ShowImages/${stageId}.jpg"
        val imagesRef = storageRef.child(imageName)
        imagesRef.delete().addOnSuccessListener {
        }.addOnFailureListener {
        }
    }

    private fun putHashMap(
        show: Show?,
        downloadUrl: String,
        isUpdate: Boolean
    ): HashMap<String, Any> {
        val showMap = HashMap<String, Any>()
        if (!isUpdate)
            showMap["_createdAt"] = Timestamp.now()

        showMap["_id"] = show?._id.toString()
        showMap["name"] = show?.name.toString()
        showMap["imageUrl"] = downloadUrl
        showMap["description"] = show?.description.toString()
        showMap["date"] = show?.date.toString()
        showMap["time"] = show?.time.toString()
        showMap["price"] = show?.price.toString()
        showMap["ageLimit"] = show?.ageLimit.toString()

        val stageIdList = ArrayList<String>()
        show?.stageId?.forEach { element ->
            stageIdList.add(element.toString())
        }

        showMap["stageId"] = stageIdList
        return showMap
    }

    private fun getAllHashMap(result: QuerySnapshot): ArrayList<Show?> {
        val showList: ArrayList<Show?> = arrayListOf()
        val documents = result.documents
        for (document in documents) {
            val createdAt =
                if (document.get("_createdAt") != null) document.get("_createdAt") as Timestamp else ""
            val id =
                if (document.get("_id") != null) document.get("_id") as String else ""
            val imgUrl =
                if (document.get("imageUrl") != null) document.get("imageUrl") as String else ""
            val name =
                if (document.get("name") != null) document.get("name") as String else ""
            val description =
                if (document.get("description") != null) document.get("description") as String else ""
            val date =
                if (document.get("date") != null) document.get("date") as String else ""
            val time = if (document.get("time") != null) document.get("time") as String else ""
            val price =
                if (document.get("price") != null) document.get("price") as String else ""
            val ageLimit =
                if (document.get("ageLimit") != null) document.get("ageLimit") as String else ""
            val stageIdRaw = document.get("stageId")
            val stageId: ArrayList<Any> = when (stageIdRaw) {
                is ArrayList<*> -> stageIdRaw as ArrayList<Any>
                else -> arrayListOf()
            }

            val show = Show(
                _createdAt = createdAt.toString(),
                _id = id,
                imageUrl = imgUrl,
                name = name,
                description = description,
                date = date,
                time = time,
                price = price,
                ageLimit = ageLimit,
                stageId = stageId,
            )
            showList?.add(show)
        }
        return showList
    }

}