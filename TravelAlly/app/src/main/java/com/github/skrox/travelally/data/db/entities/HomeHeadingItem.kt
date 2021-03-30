package com.github.skrox.travelally.data.db.entities

import android.util.Log
import android.view.View

data class HomeHeadingItem(
    val heading: String
) {
    fun onClick(view: View) {
        Log.e("clickedtrip", heading)
//        val navController = Navigation.findNavController(view)
//        val bundle = bundleOf("tripId" to this.id)
//        navController.navigate(R.id.action_nav_home_to_tripDetailFragment, bundle)
    }

}