package com.github.skrox.travelally.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.skrox.travelally.data.db.AppDatabase
import com.github.skrox.travelally.data.db.entities.Trip
import com.github.skrox.travelally.data.network.MyApi
import com.github.skrox.travelally.data.network.SafeApiRequest
import com.github.skrox.travelally.data.network.responses.TripsResponse
import com.github.skrox.travelally.data.preferences.PreferenceProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TripsRepository (private val api: MyApi, private val db: AppDatabase,
                       private val prefs: PreferenceProvider): SafeApiRequest(){

    private val authMap = mapOf<String,String?>("Authorization" to "Token "+prefs.getToken())

    suspend fun getPopularTrips(): List<Trip>{
        //TODO:check if fetch needed by db

        val response=apiRequest { api.getPopularTrips(authMap) }
        return response.trips
    }

    suspend fun getTripsNearMe(): List<Trip>{
        //TODO:check if fetch needed by db

        val queryMap = mapOf<String,Double>("lat" to 5.0, "lon" to 5.0, "radius" to 1.0)
        val response=apiRequest { api.getTripsNearMe(authMap, queryMap) }
        return response.trips
    }
}