package com.ferdidrgn.theatreticket.commonModels.dummyData

import android.net.Uri

data class User(
    var _createdAt: String? = null,
    var _id: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var fullName: String? = null,
    var phoneNumber: String? = null,
    var age: String? = null,
    var imgUrl: String? = null,
    var addOrUpdateImgUrl: Uri? = null,
    var eMail: String? = null,
    var isActivite: Boolean? = null,
    var role: String? = null,
    var token: String? = null,
    var fcmToken: String? = null
)