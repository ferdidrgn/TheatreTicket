package com.ferdidrgn.theatreticket.data.repository

import android.net.Uri
import com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData.User
import com.ferdidrgn.theatreticket.tools.enums.Response
import com.ferdidrgn.theatreticket.tools.showToast
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class UserFirebaseQueries {

    private val fireStoreUserRef = Firebase.firestore.collection("User")
    var storageRef = Firebase.storage.reference
    fun currentUserId(): String? {
        return FirebaseAuth.getInstance().uid
    }

    fun currentUserDetails(): DocumentReference? {
        return currentUserId()?.let {
            FirebaseFirestore.getInstance().collection("User").document(it)
        }
    }

    fun allUserCollectionReference(): CollectionReference? {
        return FirebaseFirestore.getInstance().collection("User")
    }

    fun addUser(user: User?, status: (Boolean) -> Unit) {
        val downloadUrl = putStrogeImage(
            user?._id.toString(),
            user?.addOrUpdateImgUrl,
            user?.imgUrl.toString()
        )

        val userMap = putHashMap(user, downloadUrl, false)

        fireStoreUserRef.add(userMap).addOnSuccessListener {
            status.invoke(true)
        }.addOnFailureListener {
            status.invoke(false)
        }
    }

    fun updateOrAddUser(user: User?, status: (Boolean) -> Unit) {
        var documentId = ""
        fireStoreUserRef.whereEqualTo("_id", user?._id).get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    addUser(user) { response ->
                        when (response) {
                            true -> status.invoke(true)
                            false -> status.invoke(false)
                        }
                    }
                } else {
                    if (result != null) {
                        val documents = result.documents
                        for (document in documents) {
                            documentId = document.id
                        }
                        updateUser(user, documentId) { response ->
                            when (response) {
                                true -> status.invoke(true)
                                false -> status.invoke(false)
                            }
                        }
                    }
                }
            }.addOnFailureListener {
                status.invoke(false)
            }
    }

    fun updateUser(user: User?, documentId: String, status: (Boolean) -> Unit) {

        val downloadUrl = putStrogeImage(
            user?._id.toString(),
            user?.addOrUpdateImgUrl,
            user?.imgUrl.toString()
        )

        val userMap = putHashMap(user, downloadUrl, true)

        fireStoreUserRef.document(documentId).update(userMap)
            .addOnSuccessListener {
                status.invoke(true)
            }.addOnFailureListener {
                status.invoke(false)
            }
    }

    fun checkRole(userId: String, status: (Response, String?) -> Unit) {
        var role = ""

        fireStoreUserRef.whereEqualTo("_id", userId).get()
            .addOnSuccessListener { result ->
                if (result.isEmpty || result == null) {
                    status.invoke(Response.Empty, null)
                } else {
                    val documents = result.documents
                    for (document in documents) {
                        role =
                            if (document.get("role") != null) document.get("role") as String else ""
                    }
                    status.invoke(Response.ThereIs, role)
                }
            }.addOnFailureListener {
                status.invoke(Response.ServerError, null)
            }
    }

    fun checkUserId(userId: String?, status: (Response, User?) -> Unit) {
        checkList("_id", userId.toString()) { response, user ->
            status.invoke(response, user)
        }
    }

    fun deleteUser(
        userId: String,
        userFirstName: String,
        status: (Boolean) -> Unit
    ) {
        FirebaseAuth.getInstance().currentUser?.delete()?.addOnSuccessListener {
            status.invoke(true)
        }?.addOnFailureListener {
            status.invoke(false)
        }

        fireStoreUserRef.document(currentUserId()!!).delete()
            .addOnSuccessListener {
                status.invoke(true)
            }.addOnFailureListener {
                status.invoke(false)
            }

        CoroutineScope(Dispatchers.IO).launch {
            val shoQuery = fireStoreUserRef
                .whereEqualTo("_id", userId)
                .whereEqualTo("firstName", userFirstName)
                .get()
                .await()
            if (shoQuery.documents.isNotEmpty()) {
                for (document in shoQuery) {
                    try {
                        fireStoreUserRef.document(document.id).delete().await()
                        /*personCollectionRef.document(document.id).update(mapOf(
                            "firstName" to FieldValue.delete()
                        )).await()*/
                        deleteStrogeImage(userId.toString())
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
    }


    private fun putStrogeImage(
        userId: String,
        userAddOrUpdateImgUrl: Uri?,
        userImgUrl: String
    ): String {
        val imageName = "UserImages/${userId}.jpg"
        val imagesRef = storageRef.child(imageName)
        var downloadUrl = ""
        if (userAddOrUpdateImgUrl != null) {
            imagesRef.putFile(userAddOrUpdateImgUrl).addOnSuccessListener {
                Firebase.storage.reference.child(imageName).downloadUrl.addOnSuccessListener { uri ->
                    downloadUrl = uri.toString()
                }
            }
        }
        downloadUrl = if (downloadUrl == "") userImgUrl else downloadUrl
        return downloadUrl
    }

    private fun putHashMap(
        user: User?,
        downloadUrl: String,
        isUpdate: Boolean
    ): HashMap<String, Any> {
        val userMap = HashMap<String, Any>()
        if (!isUpdate)
            userMap["_createdAt"] = Timestamp.now()

        userMap["_id"] = user?._id.toString()
        userMap["firstName"] = user?.firstName.toString()
        userMap["lastName"] = user?.lastName.toString()
        userMap["fullName"] = user?.fullName.toString()
        userMap["phoneNumber"] = user?.phoneNumber.toString()
        userMap["photoUrl"] = downloadUrl
        userMap["isActivite"] = user?.isActivite.toString().toBoolean()
        userMap["age"] = user?.age.toString()
        userMap["eMail"] = user?.eMail.toString()
        userMap["fcmToken"] = user?.fcmToken.toString()
        userMap["role"] = user?.role.toString()
        return userMap
    }

    private fun deleteStrogeImage(userId: String) {
        val imageName = "UserImages/${userId}.jpg"
        val imagesRef = storageRef.child(imageName)
        imagesRef.delete().addOnSuccessListener {
            showToast("Resim silindi")
        }.addOnFailureListener {
            showToast("Resim silinemedi")
        }
    }

    private fun checkList(
        key: String,
        value: String,
        status: (Response, User?) -> Unit
    ) {

        var userInfoList: User? = null
        var notEqual: Boolean? = null

        fireStoreUserRef.whereEqualTo(key, value).get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    status.invoke(Response.Empty, null)
                } else {
                    if (result != null) {
                        val documents = result.documents
                        notEqual@ for (document in documents) {
                            val customerId =
                                if (document.get("_id") != null) document.get("_id") as String else ""

                            val userFirstName =
                                if (document.get("firstName") != null) document.get(
                                    "firstName"
                                ) as String else ""

                            val userLastName =
                                if (document.get("lastName") != null) document.get(
                                    "lastName"
                                ) as String else ""

                            val fullName =
                                if (document.get("fullName") != null) document.get(
                                    "fullName"
                                ) as String else ""

                            val phoneNumber =
                                if (document.get("phoneNumber") != null) document.get(
                                    "phoneNumber"
                                ) as String else ""

                            val isActivite =
                                if (document.get("isActivite") != null) document.get(
                                    "isActivite"
                                ) as Boolean else false
                            val fcmToken =
                                if (document.get("fcmToken") != null) document.get(
                                    "fcmToken"
                                ) as String else ""
                            val age =
                                if (document.get("age") != null) document.get("age") as String else ""
                            val eMail =
                                if (document.get("eMail") != null) document.get(
                                    "eMail"
                                ) as String else ""
                            val role =
                                if (document.get("role") != null) document.get("role") as String else ""
                            val photoUrl =
                                if (document.get("photoUrl") != null) document.get(
                                    "photoUrl"
                                ) as String else ""

                            userInfoList = User(
                                _id = customerId,
                                fcmToken = fcmToken,
                                firstName = userFirstName,
                                lastName = userLastName,
                                fullName = fullName,
                                phoneNumber = phoneNumber,
                                isActivite = isActivite,
                                age = age,
                                eMail = eMail,
                                role = role,
                                imgUrl = photoUrl
                            )
                        }

                        if (notEqual == true) {
                            status.invoke(Response.NotEqual, null)
                        } else {
                            status.invoke(Response.ThereIs, userInfoList)
                        }
                    }
                }
            }.addOnFailureListener {
                status.invoke(Response.ServerError, null)
            }
    }
}