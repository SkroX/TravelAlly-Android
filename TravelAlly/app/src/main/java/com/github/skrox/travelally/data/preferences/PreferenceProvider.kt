package com.github.skrox.travelally.data.preferences

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager
import javax.inject.Inject
import javax.inject.Singleton

private const val KEY_TOKEN = "token"
private const val KEY_LAT = "lat"
private const val KEY_LON = "lon"
private const val KEY_RADIUS = "radius"
private const val KEY_USER_ID = "userId"
private const val KEY_LOC_NAME = "locname"


@Singleton
class PreferenceProvider @Inject constructor(
    context: Context
) {

    private val appContext = context.applicationContext

    private val preference: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)


    fun saveToken(token: String) {
        Log.e("token save prefs", token)

        preference.edit().putString(
            KEY_TOKEN,
            token
        ).apply()
    }

    fun getToken(): String? {
//        Log.e("prefpro", preference.getString(KEY_TOKEN, null))
        return preference.getString(KEY_TOKEN, null)
    }

    fun saveId(id: String) {
        preference.edit().putString(
            KEY_USER_ID,
            id
        ).apply()
    }

    fun getId(): String? {
        return preference.getString(KEY_USER_ID, null)
    }

    fun saveLat(lat: Double) {
        preference.edit().putString(
            KEY_LAT,
            lat.toString()
        ).apply()
    }

    fun saveLon(lon: Double) {
        preference.edit().putString(
            KEY_LON,
            lon.toString()
        ).apply()
    }

    fun saveRadius(radius: String) {
        preference.edit().putString(
            KEY_RADIUS,
            radius.toString()
        ).apply()
    }

    fun saveLocName(name: String) {
        preference.edit().putString(
            KEY_LOC_NAME,
            name
        ).apply()
    }

    fun getLocName() = preference.getString(KEY_LOC_NAME, "Delhi, India")
    fun getLat() = preference.getString(KEY_LAT, "28.7041")?.toDoubleOrNull()
    fun getLon() = preference.getString(KEY_LON, "77.1025")?.toDoubleOrNull()
    fun getRadius() = preference.getString(KEY_RADIUS, "200")?.toDoubleOrNull()

}