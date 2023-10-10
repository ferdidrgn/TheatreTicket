package com.ferdidrgn.theatreticket.forFirebaseQueries

import com.ferdidrgn.theatreticket.commonModels.dummyData.Sell
import com.ferdidrgn.theatreticket.commonModels.dummyData.Customer
import com.ferdidrgn.theatreticket.enums.Response
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ForFirebaseQueries {

    private val fireStore = Firebase.firestore

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

    fun checkBuyTicketCustomerId(customerId: String, status: (String) -> Unit) {
        var statusTree = ""

        fireStore.collection("Sell").whereEqualTo("customerId", customerId).get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    statusTree = Response.Empty.response
                    status.invoke(statusTree)
                } else {
                    statusTree = Response.ThereIs.response
                    status.invoke(statusTree)
                }
            }.addOnFailureListener {
                statusTree = Response.ServerError.response
                status.invoke(statusTree)
            }
    }

    fun checkSearchBuyTicket(
        customer: Customer,
        status: (String, HashMap<String, Any>) -> Unit
    ) {
        var statusTree = ""
        val sellData: HashMap<String, Any> = HashMap()
        fireStore.collection("Sell").whereEqualTo("customerPhone", customer.phoneNumber)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    statusTree = Response.ServerError.response
                    status.invoke(statusTree, sellData)
                } else {
                    if (value != null) {
                        if (!value.isEmpty) {
                            statusTree = Response.ThereIs.response
                            val documents = value.documents
                            for (document in documents) {
                                sellData["_createdAt"] = document.get("_createdAt") as String
                                sellData["_id"] = document.get("_id") as String
                                sellData["customerId"] = document.get("customerId") as String
                                sellData["showId"] = document.get("showId") as String
                            }
                            status.invoke(statusTree, sellData)
                        } else {
                            statusTree = Response.Empty.response
                            status.invoke(statusTree, sellData)
                        }
                    } else {
                        statusTree = Response.Empty.response
                        status.invoke(statusTree, sellData)
                    }
                }
                status.invoke(statusTree, sellData)
            }
    }

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

    fun saveSales(sell: Sell, status: (Boolean) -> Unit) {
        val salesMap = HashMap<String, Any>()
        salesMap["_createdAt"] = Timestamp.now()
        salesMap["_id"] = sell._id.toString()
        salesMap["customerId"] = sell.customerId.toString()
        salesMap["showId"] = sell.showId.toString()

        fireStore.collection("Sell").add(salesMap).addOnSuccessListener {
            status.invoke(true)
        }.addOnFailureListener {
            status.invoke(false)
        }
    }
}