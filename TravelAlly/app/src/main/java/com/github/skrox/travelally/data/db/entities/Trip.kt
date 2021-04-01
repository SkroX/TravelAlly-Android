package com.github.skrox.travelally.data.db.entities

import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.github.skrox.travelally.R


data class Trip(
    val id: Int, var start_time: String, var end_time: String,
    val additional_info: String?, val extra_people: List<Int>,
    val start_lat: Double, val start_lon: Double,
    val end_lat: Double, val end_lon: Double,
    val start_name: String?, val dest_name: String?,
    val voters: List<Int>, val organizer: Int, var image: String?
) {
    fun onClick(view: View) {
        Log.e("clickedtrip", this.start_time)
        val navController = Navigation.findNavController(view)
        val bundle = bundleOf("tripId" to this.id)
        navController.navigate(R.id.action_nav_home_to_tripDetailFragment, bundle)
    }

}


//{
//    "id": 7,
//    "start_time": "2020-11-11T04:54:00Z",
//    "end_time": "2020-12-11T04:45:00Z",
//    "additional_info": null,
//    "extra_people": [
//    3
//    ],
//    "start_lat": "12.000000",
//    "start_lon": "23.000000",
//    "end_lat": "12.000000",
//    "end_lon": "23123.000000",
//    "start_name": "abc",
//    "dest_name": "def",
//    "voters": [],
//    "organizer": 2,
//    "image": "/media/upload/recipe/270eb06f-578a-4fb6-b408-991c1e3b15ae.jpg"
//}