package com.ferdidrgn.theatreticket.data.repository

import com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData.User
import com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData.Sell
import com.ferdidrgn.theatreticket.util.Response
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SellFirebaseQueries {

    private val fireStoreSellRef = Firebase.firestore.collection("Sell")

    fun checkBuyTicketCustomerId(customerId: String, status: (Response) -> Unit) {

        fireStoreSellRef.whereEqualTo("customerId", customerId).get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) status.invoke(Response.Empty)
                else status.invoke(Response.ThereIs)
            }.addOnFailureListener {
                status.invoke(Response.ServerError)
            }
    }

    fun checkSearchBuyTicket(
        user: User,
        status: (Response, ArrayList<Sell?>?) -> Unit
    ) {
        val sellList: ArrayList<Sell?> = arrayListOf()
        fireStoreSellRef.whereEqualTo("customerPhone", user.phoneNumber)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    status.invoke(Response.ServerError, null)
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
                            status.invoke(Response.ThereIs, sellList)
                        } else status.invoke(Response.Empty, null)
                    } else
                        status.invoke(Response.Empty, null)
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