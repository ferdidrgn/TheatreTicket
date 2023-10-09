package com.ferdidrgn.theatreticket.forFirebaseQueries

import com.ferdidrgn.theatreticket.commonModels.dummyData.Sell
import com.ferdidrgn.theatreticket.commonModels.dummyData.Customer
import com.ferdidrgn.theatreticket.enums.Response
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ForFirebaseQueries {

    private val fireStore = Firebase.firestore

    fun checkPhoneNumber(phoneNumber: String, response: (Pair<String, String>) -> Unit) {
        var customerId: String = ""
        var statusTree: String = ""

        fireStore.collection("Customer").whereEqualTo("phoneNumber", phoneNumber)
            .addSnapshotListener { value, error ->
                println("kaç kere girdin")
                if (error != null) {
                    statusTree = Response.ServerError.response
                    response.invoke(Pair(customerId, statusTree))
                } else {
                    if (value != null) {
                        if (!value.isEmpty) {
                            statusTree = Response.ThereIs.response
                            val documents = value.documents
                            for (document in documents) {
                                customerId =
                                    if (document.get("_id") != null) document.get("_id") as String
                                    else ""
                            }
                            response.invoke(Pair(customerId, statusTree))
                        } else {
                            statusTree = Response.Empty.response
                            response.invoke(Pair(customerId, statusTree))
                        }
                    }
                }
            }
    }

    fun checkBuyTicketCustomerId(customerId: String, status: (String) -> Unit) {
        var statusTree = ""

        fireStore.collection("Sell").whereEqualTo("customerId", customerId)
            .addSnapshotListener { value, error ->
                statusTree = if (error != null) Response.ServerError.response
                else {
                    if (value != null) {
                        if (!value.isEmpty) Response.ThereIs.response
                        else Response.Empty.response
                    } else Response.Empty.response
                }
                status.invoke(statusTree)
            }
    }

    suspend fun checkSearchBuyTicket(
        customer: Customer,
        status: (Pair<String, HashMap<String, Any>>) -> Unit
    ) {
        var statusTree = ""
        var sellData: HashMap<String, Any> = HashMap()
        fireStore.collection("Sell").whereEqualTo("customerPhone", customer.phoneNumber)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    statusTree = Response.ServerError.response
                    status.invoke(Pair(statusTree, sellData))
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
                            status.invoke(Pair(statusTree, sellData))
                        } else {
                            statusTree = Response.Empty.response
                            status.invoke(Pair(statusTree, sellData))
                        }
                    } else {
                        statusTree = Response.Empty.response
                        status.invoke(Pair(statusTree, sellData))
                    }
                }
                status.invoke(Pair(statusTree, sellData))
            }
    }

    fun addCustomer(customer: Customer?, status: (Boolean) -> Unit) {
        val customerMap = HashMap<String, Any>()
        customerMap["_createdAt"] = Timestamp.now()
        customerMap["_id"] = customer?._id.let { it.toString() }
        customerMap["firstName"] = customer?.firstName.let { it.toString() }
        customerMap["lastName"] = customer?.lastName.let { it.toString() }
        customerMap["phoneNumber"] = customer?.phoneNumber.let { it.toString() }
        customerMap["isActivite"] = customer?.isActivite.let { it.toString() }
        customerMap["age"] = customer?.age.let { it.toString() }
        customerMap["eMail"] = customer?.eMail.let { it.toString() }

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