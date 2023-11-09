package com.ferdidrgn.theatreticket.commonModels.dummyData

data class Stage(
    var _id: String? = null,
    var name: String? = null,
    var description: String? = null,
    var communication: String? = null,
    var capacity: String? = null,
    var location: String? = null,
    val locationLat: Double? = null,
    val locationLng: Double? = null,
    val seatId: String? = null,
    var seatColumnCount: Int? = null,
    var seatRowCount: Int? = null,
)
