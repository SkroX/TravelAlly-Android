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

    suspend fun getPopularTrips(): List<Trip>{
        //TODO:check if fetch needed by db

        val map = mapOf<String,String?>("Authorization" to "Token "+prefs.getToken())
        val response=apiRequest { api.getPopularTrips(map) }
        return response.trips
    }

}