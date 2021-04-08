package com.github.skrox.travelally.data.repositories

import android.util.Log
import com.github.skrox.travelally.data.db.entities.Trip
import com.github.skrox.travelally.data.db.entities.User
import com.github.skrox.travelally.data.db.entities.Vote
import com.github.skrox.travelally.data.network.MyApi
import com.github.skrox.travelally.data.network.SafeApiRequest
import com.github.skrox.travelally.data.network.postobjects.PostTrip
import com.github.skrox.travelally.data.preferences.PreferenceProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TripsRepository @Inject constructor(
    private val api: MyApi,
//                       private val db: AppDatabase,
    private val prefs: PreferenceProvider
) : SafeApiRequest() {


    suspend fun getPopularTrips(): List<Trip> {
        //TODO:check if fetch needed by db
        val authMap = mapOf<String, String?>("Authorization" to "Token " + prefs.getToken())
        val response = apiRequest { api.getPopularTrips(authMap) }
        Log.e("triprepo", response.trips.size.toString())
        return response.trips


    }

    suspend fun getTripsNearMe(): List<Trip> {
        val authMap = mapOf<String, String?>("Authorization" to "Token " + prefs.getToken())

        //TODO:check if fetch needed by db

        val queryMap = mapOf<String, Double>(
            "lat" to (prefs.getLat() ?: 28.7041),
            "lon" to (prefs.getLon() ?: 77.1025),
            "radius" to (prefs.getRadius() ?: 200.0)
        )

        val response = apiRequest { api.getTripsNearMe(authMap, queryMap) }
        return response.trips
    }

    suspend fun getAllTrips(): List<Trip> {
        val authMap = mapOf<String, String?>("Authorization" to "Token " + prefs.getToken())

        //TODO:check if fetch needed by db

        return apiRequest { api.getAllTrips(authMap) }
    }

    suspend fun getTrip(id: Int): Trip {
        val authMap = mapOf<String, String?>("Authorization" to "Token " + prefs.getToken())

        return apiRequest { api.getTrip(id, authMap) }
    }

    suspend fun postTrip(trip: PostTrip): Trip {
        val authMap = mapOf<String, String?>("Authorization" to "Token " + prefs.getToken())

        return apiRequest { api.postTrip(authMap, trip) }
    }

    suspend fun voteTrip(tripId: Int): Trip {
        val authMap = mapOf<String, String?>("Authorization" to "Token " + prefs.getToken())

        val userId = prefs.getId()
        Log.e("userid", userId)
        return apiRequest { api.voteTrip(authMap, Vote(tripId, userId!!)) }
    }

    suspend fun requestToJoin(tripId: Int) {
        val authMap = mapOf<String, String?>("Authorization" to "Token " + prefs.getToken())

        return apiRequest { api.requestToJoin(tripId, authMap) }
    }

    suspend fun getOrganizer(id: Int): User {
        val authMap = mapOf<String, String?>("Authorization" to "Token " + prefs.getToken())

        return apiRequest { api.getUser(id, authMap) }
    }
}