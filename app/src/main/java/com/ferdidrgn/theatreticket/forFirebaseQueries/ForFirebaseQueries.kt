package com.ferdidrgn.theatreticket.forFirebaseQueries

import com.ferdidrgn.theatreticket.commonModels.dummyData.Sell
import com.ferdidrgn.theatreticket.commonModels.dummyData.Customer
import com.ferdidrgn.theatreticket.enums.Response
import com.ferdidrgn.theatreticket.tools.ClientPreferences
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow

class ForFirebaseQueries {

    private val fireStore = Firebase.firestore

    suspend fun checkPhoneNumber(phoneNumber: String): Pair<String, String> {
        var customerId: String = ""
        var statusTree: String = ""

        fireStore.collection("Customer").whereEqualTo("phoneNumber", phoneNumber)
            .addSnapshotListener { value, error ->
                if (error != null) statusTree = Response.ServerError.response
                else {
                    if (value != null) {
                        if (!value.isEmpty) {
                            statusTree = Response.ThereIs.response
                            val documents = value.documents
                            for (document in documents) {
                                customerId =
                                    if (document.get("_id") != null) document.get("_id") as String
                                    else ""
                            }
                        } else statusTree = Response.Empty.response
                    }
                }
            }

        return Pair(statusTree, customerId)
    }

    suspend fun checkBuyTicket(customerId: String): String {
        var statusTree: String = ""

        fireStore.collection("Sell").whereEqualTo("customerId", customerId)
            .addSnapshotListener { value, error ->

                if (error != null) statusTree = Response.ServerError.response
                else {
                    if (value != null) statusTree = Response.ThereIs.response
                    else statusTree = Response.Empty.response
                }
            }
        return statusTree
    }

    suspend fun addCustomer(customer: Customer): Boolean {
        var statusTwo = false
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
            statusTwo = true
        }.addOnFailureListener {
            statusTwo = false //tekrar denemeli mesajı ver server hatası
        }

        return statusTwo
    }

    suspend fun saveSales(sell: Sell): Boolean {
        var statusTwo = false
        val salesMap = HashMap<String, Any>()
        salesMap["_createdAt"] = Timestamp.now()
        salesMap["_id"] = sell._id.toString()
        salesMap["customerId"] = sell.customerId.toString()
        salesMap["showId"] = sell.showId.toString()

        fireStore.collection("Sell").add(salesMap).addOnSuccessListener {
            statusTwo = true
        }.addOnFailureListener {
            statusTwo = false
        }

        return statusTwo
    }
}