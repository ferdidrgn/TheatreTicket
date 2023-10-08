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

    fun checkBuyTicket(customerId: String, status: (String) -> Unit) {
        var statusTree = ""

        fireStore.collection("Sell").whereEqualTo("customerId", customerId)
            .addSnapshotListener { value, error ->
                statusTree = if (error != null) Response.ServerError.response
                else {
                    if (value != null) Response.ThereIs.response
                    else Response.Empty.response
                }
                status.invoke(statusTree)
            }
    }

    fun addCustomer(customer: Customer, status: (Boolean) -> Unit) {
        val customerMap = HashMap<String, Any>()
        customerMap["_createdAt"] = Timestamp.now()
        customerMap["_id"] = customer._id
        customerMap["firstName"] = customer.firstName
        customerMap["lastName"] = customer.lastName
        customerMap["phoneNumber"] = customer.phoneNumber
        customerMap["isActivite"] = customer.isActivite
        customerMap["age"] = customer.age
        customerMap["eMail"] = customer.eMail

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