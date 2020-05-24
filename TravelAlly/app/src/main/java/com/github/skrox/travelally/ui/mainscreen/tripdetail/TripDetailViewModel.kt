package com.github.skrox.travelally.ui.mainscreen.tripdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.skrox.travelally.data.db.entities.Trip
import com.github.skrox.travelally.data.repositories.TripsRepository
import kotlinx.coroutines.launch

class TripDetailViewModel(private val tripsRepository: TripsRepository) : ViewModel() {

    var tripDetailListener: TripDetailListener? =null

    val tripId: MutableLiveData<Int> = MutableLiveData()

    val trip:MutableLiveData<Trip> = MutableLiveData()

    init {
        tripId.observeForever {
            viewModelScope.launch {
                getTrip(it)
            }
        }
    }


    private suspend fun getTrip(id:Int){

        try {
            trip.postValue(tripsRepository.getTrip(id))
        }catch (e:Exception){
            tripDetailListener?.onFailure(e.message ?: "Unknown cause")
        }

    }

}
