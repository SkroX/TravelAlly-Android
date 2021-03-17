package com.github.skrox.travelally.ui.mainscreen.userprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.skrox.travelally.data.repositories.TripsRepository
import com.github.skrox.travelally.data.repositories.UserRepository
import com.github.skrox.travelally.di.ActivityScope
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import javax.inject.Inject

@ActivityScope
@Suppress("UNCHECKED_CAST")
class UserProfileVMFactory @Inject constructor(
    private val tripsRepository: TripsRepository,
    private val userRepository: UserRepository,
    private val mGoogleSignInClient: GoogleSignInClient
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserProfileViewModel(tripsRepository, userRepository, mGoogleSignInClient) as T
    }
}