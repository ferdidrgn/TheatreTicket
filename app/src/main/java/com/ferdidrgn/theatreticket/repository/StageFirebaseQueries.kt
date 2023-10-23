package com.ferdidrgn.theatreticket.repository

import com.ferdidrgn.theatreticket.commonModels.dummyData.Stage
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class StageFirebaseQueries {

    private val fireStoreStageRef = Firebase.firestore.collection("Stage")

    fun addStage(stage: Stage, status: (Boolean) -> Unit) {
        val stageMap = HashMap<String, Any>()
        stageMap["_createdAt"] = Timestamp.now()
        stageMap["_id"] = stage._id.toString()
        stageMap["name"] = stage.name.toString()
        stageMap["capacity"] = stage.capacity.toString()
        stageMap["description"] = stage.description.toString()
        stageMap["communication"] = stage.communication.toString()
        stageMap["location"] = stage.location.toString()
        stageMap["locationLat"] = stage.locationLat.toString()
        stageMap["locationLng"] = stage.locationLng.toString()

        fireStoreStageRef.add(stageMap).addOnSuccessListener {
            status.invoke(true)
        }.addOnFailureListener {
            status.invoke(false)
        }
    }
}