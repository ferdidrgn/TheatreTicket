package com.ferdidrgn.theatreticket.repository

import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.*

class ImageFireBase {

    var fireStorage = Firebase.storage
    val storageRef = fireStorage.reference

    fun addShowImage(selectedPicture: Uri?) {
        val uuid = UUID.randomUUID()
        val imageName = "ShowImages/$uuid.jpg"
        val imagesRef = storageRef.child(imageName)
        if (selectedPicture != null) {
            imagesRef.putFile(selectedPicture).addOnSuccessListener {
                val uploadedPictureReference = Firebase.storage.reference.child(imageName)
                uploadedPictureReference.downloadUrl.addOnSuccessListener {
                    val downloadUrl = it.toString()
                }.addOnFailureListener { }
            }.addOnFailureListener { }
        } else {
            println("selectedPicture null")
        }
    }

    fun uploadShowImage() {
        val imagesRef = storageRef.child("showimages")
        val spaceRef = storageRef.child("images/space.jpg")
        val path = spaceRef.path
        val name = spaceRef.name
        val bucket = spaceRef.bucket
        val parent = spaceRef.parent
        val rootRef = spaceRef.root
    }
}