package com.github.skrox.travelally.data.network.postobjects

data class PostTrip(
    var start_time: String?, var end_time: String?,
    val additional_info: String?, val extra_people: List<Int>,
    val start_lat: Double, val start_lon: Double,
    val end_lat: Double, val end_lon: Double,
    val start_name: String?, val dest_name: String?
)