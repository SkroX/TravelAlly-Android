package com.github.skrox.travelally.ui.mainscreen.tripdetail

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.skrox.travelally.data.db.entities.Trip
import com.github.skrox.travelally.data.repositories.TripsRepository
import com.github.skrox.travelally.util.loadImage
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

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


    @SuppressLint("SimpleDateFormat")
    private suspend fun getTrip(id:Int){
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val outputFormat = SimpleDateFormat("EEE, d MMM yyyy 'at' HH:mm:ss")
        try {
            val t:Trip = tripsRepository.getTrip(id)

            var d: Date = inputFormat.parse(t.start_time)
            t.start_time = outputFormat.format(d)

            d = inputFormat.parse(t.end_time)
            t.end_time = outputFormat.format(d)

            trip.postValue(t)
        }catch (e:Exception){
            tripDetailListener?.onFailure(e.message ?: "Unknown cause")
        }

    }

}
