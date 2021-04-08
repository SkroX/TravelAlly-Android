package com.github.skrox.travelally.ui.mainscreen.home

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.skrox.travelally.data.db.entities.Trip
import com.github.skrox.travelally.data.repositories.TripsRepository
import com.github.skrox.travelally.data.repositories.UserRepository
import com.github.skrox.travelally.ui.mainscreen.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.OnCompleteListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeViewModel(
    private val userRepository: UserRepository,
    private val mGoogleSignInClient: GoogleSignInClient,
    private val tripsRepository: TripsRepository
) : ViewModel() {

    var homeListener: HomeListener? = null

    fun getuser(context: Context) = userRepository.getUser(context)

    fun logout(view: View) {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(view.context as MainActivity, OnCompleteListener<Void?> {
                // ...
                userRepository.logout()
            })
    }

    private val popularTrips = MutableLiveData<List<Trip>>()
    val _popularTrips: LiveData<List<Trip>> = popularTrips

    private val tripsNearMe = MutableLiveData<List<Trip>>()
    val _tripsNearMe: LiveData<List<Trip>> = tripsNearMe

    private val allTrips = MutableLiveData<List<Trip>>()
    val _allTrips: LiveData<List<Trip>> = allTrips

    fun loadPopularTrips() = viewModelScope.launch(Dispatchers.IO) {

        if (popularTrips.value == null) {
            try {
                val trips = tripsRepository.getPopularTrips()
                popularTrips.postValue(trips)
            } catch (e: Exception) {
                homeListener?.onFailure(e.message ?: "Unknown cause")
                e.printStackTrace()
            }
        }


    }

    fun loadTripsNearMe() = viewModelScope.launch(Dispatchers.IO) {
        if (tripsNearMe.value == null) {
            try {
                val trips = tripsRepository.getTripsNearMe()
                tripsNearMe.postValue(trips)
            } catch (e: Exception) {
                homeListener?.onFailure(e.message ?: "Unknown cause")
                e.printStackTrace()
            }
        }
    }

    fun loadAllTrips() = viewModelScope.launch(Dispatchers.IO) {
        if (allTrips.value == null) {
            try {
                val trips = tripsRepository.getAllTrips()
                allTrips.postValue(trips)
            } catch (e: Exception) {
                homeListener?.onFailure(e.message ?: "Unknown cause")
                e.printStackTrace()
            }
        }
    }

    fun voteTrip(view: View, tripid: Int) {
        Log.e("upvote", " " + tripid)
        viewModelScope.launch {
            try {
                tripsRepository.voteTrip(tripid)
            } catch (e: Exception) {
                homeListener?.onFailure(e.message ?: "Unknown cause")
                e.printStackTrace()
            }
        }
    }
}