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
        userMap["fullName"] = user?.fullName.toString()
        userMap["phoneNumber"] = user?.phoneNumber.toString()
        userMap["photoUrl"] = user?.imgUrl.toString()
        userMap["isActivite"] = user?.isActivite.toString().toBoolean()
        userMap["age"] = user?.age.toString()
        userMap["eMail"] = user?.eMail.toString()
        userMap["fcmToken"] = user?.fcmToken.toString()
        userMap["role"] = user?.role.toString()

        fireStoreUserRef.add(userMap).addOnSuccessListener {
            status.invoke(true)
        }.addOnFailureListener {
            status.invoke(false)
        }
    }

    fun updateUser(user: User?, status: (Boolean) -> Unit) {
        //MockData
        status.invoke(true)
    }

    fun checkPhoneNumber(user: User?, status: (Response, User?) -> Unit) {
        var userInfoList: User? = null
        var userFirstName = ""
        var userLastName = ""
        var age = ""
        var notEqual: Boolean? = null

        fireStoreUserRef.whereEqualTo("phoneNumber", user?.phoneNumber).get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    status.invoke(Response.Empty, null)
                } else {
                    if (result != null) {
                        val documents = result.documents
                        notEqual@ for (document in documents) {
                            val customerId =
                                if (document.get("_id") != null) document.get("_id") as String else ""

                            userFirstName =
                                if (document.get("firstName") != null) document.get("firstName") as String else ""
                            if (userFirstName.isEmpty().not()) {
                                if (userFirstName != user?.firstName)
                                    notEqual = true; break@notEqual
                            }

                            userLastName =
                                if (document.get("lastName") != null) document.get("lastName") as String else ""
                            if (userLastName.isEmpty().not()) {
                                if (userLastName != user?.lastName)
                                    notEqual = true; break@notEqual
                            }

                            val fullName =
                                if (document.get("fullName") != null) document.get("fullName") as String else ""

                            if (fullName.isEmpty().not()) {
                                if (fullName != user?.fullName)
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
                            val role =
                                if (document.get("role") != null) document.get("role") as String else ""
                            val photoUrl =
                                if (document.get("photoUrl") != null) document.get("photoUrl") as String else ""

                            userInfoList = User(
                                _id = customerId,
                                fcmToken = fcmToken,
                                firstName = userFirstName,
                                lastName = userLastName,
                                fullName = fullName,
                                phoneNumber = user?.phoneNumber,
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

    fun checkUserId(user: User?, status: (Response, User?) -> Unit) {
        var userInfoList: User? = null
        var userFirstName = ""
        var userLastName = ""
        var notEqual: Boolean? = null

        fireStoreUserRef.whereEqualTo("_id", user?._id).get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    status.invoke(Response.Empty, null)
                } else {
                    if (result != null) {
                        val documents = result.documents
                        notEqual@ for (document in documents) {
                            val phoneNumber =
                                if (document.get("phoneNumber") != null) document.get("phoneNumber") as String else ""
                            userFirstName =
                                if (document.get("firstName") != null) document.get("firstName") as String else ""
                            if (userFirstName.isEmpty().not()) {
                                if (userFirstName != user?.firstName)
                                    notEqual = true; break@notEqual
                            }

                            userLastName =
                                if (document.get("lastName") != null) document.get("lastName") as String else ""
                            if (userLastName.isEmpty().not()) {
                                if (userLastName != user?.lastName)
                                    notEqual = true; break@notEqual
                            }

                            val fullName =
                                if (document.get("fullName") != null) document.get("fullName") as String else ""

                            if (fullName.isEmpty().not()) {
                                if (fullName != user?.fullName)
                                    notEqual = true; break@notEqual
                            }

                            val isActivite =
                                if (document.get("isActivite") != null) document.get("isActivite") as Boolean else false
                            val fcmToken =
                                if (document.get("fcmToken") != null) document.get("fcmToken") as String else ""
                            val age =
                                if (document.get("age") != null) document.get("age") as String else ""
                            val eMail =
                                if (document.get("eMail") != null) document.get("eMail") as String else ""
                            val role =
                                if (document.get("role") != null) document.get("role") as String else ""
                            val photoUrl =
                                if (document.get("photoUrl") != null) document.get("photoUrl") as String else ""

                            userInfoList = User(
                                _id = user?._id,
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

    fun deleteUser(status: (Boolean) -> Unit) {
        fireStoreUserRef.document(currentUserId()!!).delete().addOnSuccessListener {
            FirebaseAuth.getInstance().currentUser?.delete()?.addOnSuccessListener {
                status.invoke(true)
            }?.addOnFailureListener {
                status.invoke(false)
            }
        }.addOnFailureListener {
            status.invoke(false)
        }
    }
}