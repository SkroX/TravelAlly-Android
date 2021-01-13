package com.github.skrox.travelally.ui.mainscreen.posttrip.temp

data class PostTripFormErrors(
    var start_time: Int = 0, var end_time: Int = 0,
    val additional_info: Int = 0, val extra_people: Int? = null
)