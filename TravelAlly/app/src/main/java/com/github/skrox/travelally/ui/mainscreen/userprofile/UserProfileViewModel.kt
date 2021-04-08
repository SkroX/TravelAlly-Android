package com.github.skrox.travelally.ui.mainscreen.userprofile

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.skrox.travelally.data.repositories.TripsRepository
import com.github.skrox.travelally.data.repositories.UserRepository
import com.github.skrox.travelally.ui.mainscreen.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.OnCompleteListener

class UserProfileViewModel(
    private val tripsRepository: TripsRepository,
    private val userRepository: UserRepository,
    private val mGoogleSignInClient: GoogleSignInClient
) : ViewModel() {
    var userProfileListener: UserProfileListener? = null
    val user: MutableLiveData<GoogleSignInAccount> = MutableLiveData()

    val radius: MutableLiveData<String> = MutableLiveData()
    val location: MutableLiveData<String> = MutableLiveData()

    init {
        radius.postValue(userRepository.getRadius().toString())
        location.postValue(userRepository.getLocation())
    }

    fun logout(view: View) {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(view.context as MainActivity, OnCompleteListener<Void?> {
                // ...
                userRepository.logout()
            })
    }

    fun getUser(context: Context) = userRepository.getUser(context)

    fun setRadius(radius: String) {
        userRepository.setRadius(radius)
    }

    fun setLat(lat: Double) {
        userRepository.setLat(lat)
    }

    fun setLon(lon: Double) {
        userRepository.setLon(lon)
    }

    fun setLocName(name: String) {
        userRepository.setLocation(name)
    }


}