package com.github.skrox.travelally.data.repositories

import android.util.Log
import com.github.skrox.travelally.data.db.entities.Trip
import com.github.skrox.travelally.data.db.entities.Vote
import com.github.skrox.travelally.data.network.MyApi
import com.github.skrox.travelally.data.network.SafeApiRequest
import com.github.skrox.travelally.data.network.postobjects.PostTrip
import com.github.skrox.travelally.data.preferences.PreferenceProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TripsRepository @Inject constructor(private val api: MyApi,
//                       private val db: AppDatabase,
                       private val prefs: PreferenceProvider): SafeApiRequest(){

    private val authMap = mapOf<String,String?>("Authorization" to "Token "+prefs.getToken())

    suspend fun getPopularTrips(): List<Trip>{
        //TODO:check if fetch needed by db

        val response=apiRequest { api.getPopularTrips(authMap) }
        Log.e("triprepo", response.trips.size.toString())
        return response.trips


    }

    suspend fun getTripsNearMe(): List<Trip>{
        //TODO:check if fetch needed by db

        val  queryMap = mapOf<String,Double>("lat" to (prefs.getLat() ?: 0.0), "lon" to (prefs.getLon() ?: 0.0), "radius" to (prefs.getRadius() ?: 5000.0) )

        val response=apiRequest { api.getTripsNearMe(authMap, queryMap) }
        return response.trips
    }

    suspend fun getAllTrips(): List<Trip>{
        //TODO:check if fetch needed by db

        return apiRequest { api.getAllTrips(authMap) }
    }

    suspend fun getTrip(id: Int): Trip{
        return apiRequest { api.getTrip(id,authMap) }
    }

    suspend fun postTrip(trip: PostTrip): Trip{
        return apiRequest { api.postTrip(authMap,trip) }
    }

    suspend fun voteTrip(tripId: Int): Trip{
        val userId = prefs.getId()
        return apiRequest { api.voteTrip(authMap, Vote(tripId,userId!!)) }
    }

    suspend fun requestToJoin(tripId: Int){
        return apiRequest { api.requestToJoin(tripId, authMap) }
    }
}