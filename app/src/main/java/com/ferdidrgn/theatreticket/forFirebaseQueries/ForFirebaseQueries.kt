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
        status: (String, ArrayList<Sell>?) -> Unit
    ) {
        var statusTree = ""
        val sellList = ArrayList<Sell>()
        fireStore.collection("Sell").whereEqualTo("customerPhone", customer.phoneNumber)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    statusTree = Response.ServerError.response
                    status.invoke(statusTree, null)
                } else {
                    if (value != null) {
                        if (!value.isEmpty) {
                            val documents = value.documents
                            for (document in documents) {
                                val createdAt = document.get("_createdAt") as Timestamp
                                val id = document.get("_id") as String
                                val customerId = document.get("customerId") as String
                                val showId = document.get("showId") as String
                                val customerFullName = document.get("customerFullName") as String
                                val customerPhone = document.get("customerPhone") as String
                                val showName = document.get("showName") as String
                                val showDate = document.get("showDate") as String
                                val showTime = document.get("showTime") as String
                                val showPrice = document.get("showPrice") as String
                                val showSeat = document.get("showSeat") as String
                                val stageName = document.get("stageName") as String
                                val stageLocation = document.get("stageLocation") as String

                                val sell = Sell(
                                    _createdAt = createdAt.toString(),
                                    _id = id,
                                    customerId = customerId,
                                    showId = showId,
                                    customerFullName = customerFullName,
                                    customerPhone = customerPhone,
                                    showName = showName,
                                    showDate = showDate,
                                    showTime = showTime,
                                    showPrice = showPrice,
                                    showSeat = showSeat,
                                    stageName = stageName,
                                    stageLocation = stageLocation
                                )
                                sellList.add(sell)
                            }
                            statusTree = Response.ThereIs.response
                            status.invoke(statusTree, sellList)
                        } else {
                            statusTree = Response.Empty.response
                            status.invoke(statusTree, null)
                        }
                    } else {
                        statusTree = Response.Empty.response
                        status.invoke(statusTree, null)
                    }
                }
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
        salesMap["customerFullName"] = sell.customerFullName.toString()
        salesMap["customerPhone"] = sell.customerPhone.toString()
        salesMap["showName"] = sell.showName.toString()
        salesMap["showDate"] = sell.showDate.toString()
        salesMap["showTime"] = sell.showTime.toString()
        salesMap["showPrice"] = sell.showPrice.toString()
        salesMap["showSeat"] = sell.showSeat.toString()
        salesMap["stageName"] = sell.showSeat.toString()
        salesMap["stageLocation"] = sell.showSeat.toString()

        fireStore.collection("Sell").add(salesMap).addOnSuccessListener {
            status.invoke(true)
        }.addOnFailureListener {
            status.invoke(false)
        }
    }
}