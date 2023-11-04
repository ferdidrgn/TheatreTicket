package com.ferdidrgn.theatreticket.repository

import com.ferdidrgn.theatreticket.enums.Response
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AppToolsFireBaseQueries {

    private val fireStoreAppToolsRef = Firebase.firestore.collection("AppTools")

    fun getTermsConditionOrPricavePolicy(
        whichTermsAndPrivace: String,
        status: (Response, String?) -> Unit
    ) {
        var html = ""

        fireStoreAppToolsRef.document(whichTermsAndPrivace).get()
            .addOnSuccessListener { result ->
                if (result != null) {
                    if (result.get(whichTermsAndPrivace) != null) {
                        html = result.get(whichTermsAndPrivace) as String
                        status.invoke(Response.ThereIs, html)
                    } else status.invoke(Response.Empty, null)
                } else status.invoke(Response.Empty, null)

            }.addOnFailureListener {
                status.invoke(Response.ServerError, null)
            }
    }


    fun getContactUsEmail(status: (Response, String?) -> Unit) {
        var eMail = ""
        fireStoreAppToolsRef.document("contactUsEmail").get()
            .addOnSuccessListener { result ->
                if (result != null) {
                    if (result.get("contactUsEmail") != null) {
                        eMail = result.get("contactUsEmail") as String
                        status.invoke(Response.ThereIs, eMail)
                    } else status.invoke(Response.Empty, null)
                } else status.invoke(Response.Empty, null)

            }.addOnFailureListener {
                status.invoke(Response.ServerError, null)
            }
    }

}