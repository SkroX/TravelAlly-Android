package com.github.skrox.travelally.data.preferences
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import javax.inject.Inject
import javax.inject.Singleton

private const val KEY_TOKEN = "token"
private const val KEY_LAT = "lat"
private const val KEY_LON = "lon"
private const val KEY_RADIUS = "radius"

@Singleton
class PreferenceProvider @Inject constructor(
    context: Context
) {

    private val appContext = context.applicationContext

    private val preference: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)


    fun saveToken(token: String) {
        preference.edit().putString(
            KEY_TOKEN,
            token
        ).apply()
    }

    fun getToken(): String? {
        return preference.getString(KEY_TOKEN, null)
    }

    fun saveLatLonRadius(lat: Double, lon:Double, radius:Double){
        preference.edit().putString(
            KEY_LAT,
            lat.toString()
        ).apply()
        preference.edit().putString(
            KEY_LON,
            lon.toString()
        ).apply()
        preference.edit().putString(
            KEY_RADIUS,
            radius.toString()
        ).apply()
    }

    fun getLat() = preference.getString(KEY_LAT, null)?.toDoubleOrNull()
    fun getLon() = preference.getString(KEY_LON, null)?.toDoubleOrNull()
    fun getRadius() = preference.getString(KEY_RADIUS, null)?.toDoubleOrNull()

}