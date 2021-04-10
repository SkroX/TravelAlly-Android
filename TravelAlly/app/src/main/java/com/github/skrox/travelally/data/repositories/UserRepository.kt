package com.github.skrox.travelally.data.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.skrox.travelally.data.db.entities.User
import com.github.skrox.travelally.data.network.MyApi
import com.github.skrox.travelally.data.network.SafeApiRequest
import com.github.skrox.travelally.data.network.postobjects.SendToken
import com.github.skrox.travelally.data.network.responses.AuthResponse
import com.github.skrox.travelally.data.network.responses.UserSuggestionResponse
import com.github.skrox.travelally.data.preferences.PreferenceProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val api: MyApi,
//                    private val db:AppDatabase,
    private val prefs: PreferenceProvider
) : SafeApiRequest() {
    private val user = MutableLiveData<GoogleSignInAccount?>()

    suspend fun login(id_info: String?): AuthResponse {
        return apiRequest {
            api.userLogin(
                SendToken(
                    id_info
                )
            )
        }
    }

    fun saveuser(authResponse: AuthResponse, account: GoogleSignInAccount?) {
//        db.getUserDao().upsert(authResponse.user)
        Log.e("token repo", authResponse.token)
        prefs.saveToken(authResponse.token)
        prefs.saveId(authResponse.user.id.toString())
        user.postValue(account)
    }

    fun getUserId() = prefs.getId()
    fun getUser(context: Context): LiveData<GoogleSignInAccount?> {

        if (user.value != GoogleSignIn.getLastSignedInAccount(context))
            user.postValue(GoogleSignIn.getLastSignedInAccount(context))
        return user
    }

    fun getToken() = prefs.getToken()

    fun logout() {
        user.postValue(null)
        Log.e("logged out", "success")
    }

    suspend fun getUsersSuggestion(query: String): List<UserSuggestionResponse> {
        //TODO:check if fetch needed by db

        val queryMap = mapOf<String, String>("q" to query)
        val authMap = mapOf<String, String?>("Authorization" to "Token " + prefs.getToken())


        return apiRequest { api.getUserSuggestions(authMap, queryMap) }
    }

    suspend fun getUser(id: Int): User {
        val authMap = mapOf<String, String?>("Authorization" to "Token " + prefs.getToken())
        return apiRequest { api.getUser(id, authMap) }
    }

    fun getRadius() = prefs.getRadius()
    fun setRadius(radius: String) {
        prefs.saveRadius(radius)
    }

    fun getLocation() = prefs.getLocName()
    fun setLocation(name: String) {
        prefs.saveLocName(name)
    }

    fun setLat(lat: Double) {
        prefs.saveLat(lat)
    }

    fun setLon(lon: Double) {
        prefs.saveLon(lon)
    }

}