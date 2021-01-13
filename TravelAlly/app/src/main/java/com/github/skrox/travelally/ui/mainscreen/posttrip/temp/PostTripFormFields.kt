package com.github.skrox.travelally.ui.mainscreen.posttrip.temp

data class PostTripFormFields(
    var start_time: String = "", var end_time: String ="",
    val additional_info: String ="", val extra_people: List<Int>? = null
)