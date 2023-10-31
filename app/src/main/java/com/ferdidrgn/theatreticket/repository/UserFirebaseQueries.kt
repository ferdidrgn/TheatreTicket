package com.ferdidrgn.theatreticket.repository

import com.ferdidrgn.theatreticket.commonModels.dummyData.User
import com.ferdidrgn.theatreticket.enums.Response
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class UserFirebaseQueries {

    private val fireStoreUserRef = Firebase.firestore.collection("User")

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
        val userMap = HashMap<String, Any>()
        userMap["_createdAt"] = Timestamp.now()
        userMap["_id"] = user?._id.toString()
        userMap["firstName"] = user?.firstName.toString()
        userMap["lastName"] = user?.lastName.toString()
        userMap["phoneNumber"] = user?.phoneNumber.toString()
        userMap["isActivite"] = user?.isActivite.toString().toBoolean()
        userMap["age"] = user?.age.toString()
        userMap["eMail"] = user?.eMail.toString()
        userMap["fcmToken"] = user?.fcmToken.toString()

        fireStoreUserRef.add(userMap).addOnSuccessListener {
            status.invoke(true)
        }.addOnFailureListener {
            status.invoke(false)
        }
    }

    fun checkPhoneNumber(user: User?, status: (String, User?) -> Unit) {
        var statusTree = ""
        var userInfoList: User? = null
        var userFirstName = ""
        var userLastName = ""
        var age = ""
        var notEqual: Boolean? = null

        fireStoreUserRef.whereEqualTo("phoneNumber", user?.phoneNumber).get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    statusTree = Response.Empty.response
                    status.invoke(statusTree, null)
                } else {
                    if (result != null) {
                        val documents = result.documents
                        notEqual@ for (document in documents) {
                            val customerId =
                                if (document.get("_id") != null) document.get("_id") as String else ""
                            userFirstName =
                                if (document.get("firstName") != null) document.get("firstName") as String else ""
                            if (userFirstName != user?.firstName) {
                                notEqual = true; break@notEqual
                            }
                            userLastName =
                                if (document.get("lastName") != null) document.get("lastName") as String else ""
                            if (userLastName != user?.lastName) {
                                notEqual = true; break@notEqual
                            }
                            val isActivite =
                                if (document.get("isActivite") != null) document.get("isActivite") as Boolean else false
                            val fcmToken =
                                if (document.get("fcmToken") != null) document.get("fcmToken") as String else ""
                            age =
                                if (document.get("age") != null) document.get("age") as String else ""
                            val eMail =
                                if (document.get("eMail") != null) document.get("eMail") as String else ""

                            userInfoList = User(
                                _id = customerId,
                                fcmToken = fcmToken,
                                firstName = userFirstName,
                                lastName = userLastName,
                                phoneNumber = user?.phoneNumber,
                                isActivite = isActivite,
                                age = age,
                                eMail = eMail
                            )
                        }

                        if (notEqual == true) {
                            statusTree = Response.NotEqual.response
                            status.invoke(statusTree, null)
                        } else {
                            statusTree = Response.ThereIs.response
                            status.invoke(statusTree, userInfoList)
                        }
                    }
                }
            }.addOnFailureListener {
                statusTree = Response.ServerError.response
                status.invoke(statusTree, null)
            }
    }
}