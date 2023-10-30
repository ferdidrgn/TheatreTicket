package com.ferdidrgn.theatreticket.repository

import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ImageFireBase {

    var fireStorage = Firebase.storage
    val storageRef = fireStorage.reference

    fun addImage(selectedPicture: Uri?) {
        val imagesRef = storageRef.child("ShowImages/1.jpg")
        if (selectedPicture != null) {
            imagesRef.putFile(selectedPicture)
        }else{
            println("selectedPicture null")
        }
    }

    fun uploadImage() {
        val imagesRef = storageRef.child("showimages")
        val spaceRef = storageRef.child("images/space.jpg")
        val path = spaceRef.path
        val name = spaceRef.name
        val bucket = spaceRef.bucket
        val parent = spaceRef.parent
        val rootRef = spaceRef.root
    }
}