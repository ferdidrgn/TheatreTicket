package com.ferdidrgn.theatreticket.repository

import com.ferdidrgn.theatreticket.commonModels.dummyData.Customer
import com.ferdidrgn.theatreticket.enums.Response
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CustomerFirebaseQueries {

    private val fireStore = Firebase.firestore

    fun addCustomer(customer: Customer?, status: (Boolean) -> Unit) {
        val customerMap = HashMap<String, Any>()
        customerMap["_createdAt"] = Timestamp.now()
        customerMap["_id"] = customer?._id.toString()
        customerMap["firstName"] = customer?.firstName.toString()
        customerMap["lastName"] = customer?.lastName.toString()
        customerMap["phoneNumber"] = customer?.phoneNumber.toString()
        customerMap["isActivite"] = customer?.isActivite.toString().toBoolean()
        customerMap["age"] = customer?.age.toString()
        customerMap["eMail"] = customer?.eMail.toString()

        fireStore.collection("Customer").add(customerMap).addOnSuccessListener {
            status.invoke(true)
        }.addOnFailureListener {
            status.invoke(false)
        }
    }

    fun checkPhoneNumber(phoneNumber: String, status: (String, String) -> Unit) {
        var customerId: String = ""
        var statusTree: String = ""

        fireStore.collection("Customer").whereEqualTo("phoneNumber", phoneNumber).get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    statusTree = Response.Empty.response
                    status.invoke(statusTree, customerId)
                } else {
                    if (result != null) {
                        val documents = result.documents
                        for (document in documents) {
                            customerId =
                                if (document.get("_id") != null) document.get("_id") as String
                                else ""
                        }
                        statusTree = Response.ThereIs.response
                        status.invoke(statusTree, customerId)
                    }
                }
            }.addOnFailureListener {
                statusTree = Response.ServerError.response
                status.invoke(statusTree, customerId)
            }
    }
}