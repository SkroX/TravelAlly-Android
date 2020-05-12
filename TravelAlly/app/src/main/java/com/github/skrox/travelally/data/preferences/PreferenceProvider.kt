package com.github.skrox.travelally.data.preferences
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

private const val KEY_SAVED_AT = "token"

class PreferenceProvider(
    context: Context
) {

    private val appContext = context.applicationContext

    private val preference: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)


    fun saveToken(token: String) {
        preference.edit().putString(
            KEY_SAVED_AT,
            token
        ).apply()
    }

    fun getToken(): String? {
        return preference.getString(KEY_SAVED_AT, null)
    }

}