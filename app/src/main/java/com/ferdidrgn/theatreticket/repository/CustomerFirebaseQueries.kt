package com.ferdidrgn.theatreticket.repository

import com.ferdidrgn.theatreticket.commonModels.dummyData.Customer
import com.ferdidrgn.theatreticket.commonModels.dummyData.Sell
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

    fun checkPhoneNumber(customer: Customer?, status: (String, Customer?) -> Unit) {
        var statusTree: String = ""
        var customerInfoList: Customer? = null
        var customerFirstName: String = ""
        var customerLastName: String = ""
        var age: String = ""
        var notEqual: Boolean? = null

        fireStore.collection("Customer").whereEqualTo("phoneNumber", customer?.phoneNumber).get()
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
                            customerFirstName =
                                if (document.get("firstName") != null) document.get("firstName") as String else ""
                            if (customerFirstName != customer?.firstName) {
                                notEqual = true; break@notEqual
                            }
                            customerLastName =
                                if (document.get("lastName") != null) document.get("lastName") as String else ""
                            if (customerLastName != customer?.lastName) {
                                notEqual = true; break@notEqual
                            }
                            val isActivite =
                                if (document.get("isActivite") != null) document.get("isActivite") as Boolean else false
                            age =
                                if (document.get("age") != null) document.get("age") as String else ""
                            val eMail =
                                if (document.get("eMail") != null) document.get("eMail") as String else ""

                            customerInfoList = Customer(
                                _id = customerId,
                                firstName = customerFirstName,
                                lastName = customerLastName,
                                phoneNumber = customer?.phoneNumber,
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
                            status.invoke(statusTree, customerInfoList)
                        }
                    }
                }
            }.addOnFailureListener {
                statusTree = Response.ServerError.response
                status.invoke(statusTree, null)
            }
    }
}