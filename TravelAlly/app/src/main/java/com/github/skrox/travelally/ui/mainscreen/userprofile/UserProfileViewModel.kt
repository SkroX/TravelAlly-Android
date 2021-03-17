package com.github.skrox.travelally.ui.mainscreen.userprofile

import android.view.View
import androidx.lifecycle.ViewModel
import com.github.skrox.travelally.data.repositories.TripsRepository
import com.github.skrox.travelally.data.repositories.UserRepository
import com.github.skrox.travelally.ui.mainscreen.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.OnCompleteListener

class UserProfileViewModel(
    private val tripsRepository: TripsRepository,
    private val userRepository: UserRepository,
    private val mGoogleSignInClient: GoogleSignInClient
) : ViewModel() {
    var userProfileListener: UserProfileListener? = null

    fun logout(view: View){
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(view.context as MainActivity, OnCompleteListener<Void?> {
                // ...
                userRepository.logout()
            })
    }

}