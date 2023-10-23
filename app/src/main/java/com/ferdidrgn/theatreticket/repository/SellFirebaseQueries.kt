package com.ferdidrgn.theatreticket.repository

import com.ferdidrgn.theatreticket.commonModels.dummyData.Customer
import com.ferdidrgn.theatreticket.commonModels.dummyData.Sell
import com.ferdidrgn.theatreticket.enums.Response
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SellFirebaseQueries {

    private val fireStoreSellRef = Firebase.firestore.collection("Sell")

    fun checkBuyTicketCustomerId(customerId: String, status: (String) -> Unit) {
        var statusTree = ""

        fireStoreSellRef.whereEqualTo("customerId", customerId).get()
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
        status: (String, ArrayList<Sell?>?) -> Unit
    ) {
        var statusTree = ""
        val sellList: ArrayList<Sell?> = arrayListOf()
        fireStoreSellRef.whereEqualTo("customerPhone", customer.phoneNumber)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    statusTree = Response.ServerError.response
                    status.invoke(statusTree, null)
                } else {
                    if (value != null) {
                        if (!value.isEmpty) {
                            val documents = value.documents
                            for (document in documents) {
                                val createdAt =
                                    if (document.get("_createdAt") != null) document.get("_createdAt") as Timestamp else ""
                                val id =
                                    if (document.get("_id") != null) document.get("_id") as String else ""
                                val customerId =
                                    if (document.get("customerId") != null) document.get("customerId") as String else ""
                                val showId =
                                    if (document.get("showId") != null) document.get("showId") as String else ""
                                val customerFullName =
                                    if (document.get("customerFullName") != null) document.get("customerFullName") as String else ""
                                val customerPhone =
                                    if (document.get("customerPhone") != null) document.get("customerPhone") as String else ""
                                val showName =
                                    if (document.get("showName") != null) document.get("showName") as String else ""
                                val showDate =
                                    if (document.get("showDate") != null) document.get("showDate") as String else ""
                                val showTime =
                                    if (document.get("showTime") != null) document.get("showTime") as String else ""
                                val showPrice =
                                    if (document.get("showPrice") != null) document.get("showPrice") as String else ""
                                val showSeat =
                                    if (document.get("showSeat") != null) document.get("showSeat") as String else ""
                                val stageName =
                                    if (document.get("stageName") != null) document.get("stageName") as String else ""
                                val stageLocation =
                                    if (document.get("stageLocation") != null) document.get("stageLocation") as String else ""
                                val locationLat =
                                    if (document.get("locationLat") != null) document.get("locationLat") as String else ""
                                val locationLng =
                                    if (document.get("locationLng") != null) document.get("locationLng") as String else ""

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
                                    stageLocation = stageLocation,
                                    locationLat = locationLat,
                                    locationLng = locationLng
                                )
                                sellList?.add(sell)
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

    fun addSales(sell: Sell, status: (Boolean) -> Unit) {
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
        salesMap["stageLat"] = sell.showSeat.toString()
        salesMap["stageLong"] = sell.showSeat.toString()

        fireStoreSellRef.add(salesMap).addOnSuccessListener {
            status.invoke(true)
        }.addOnFailureListener {
            status.invoke(false)
        }
    }
}