package com.github.skrox.travelally.ui.mainscreen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.skrox.travelally.data.repositories.TripsRepository
import com.github.skrox.travelally.data.repositories.UserRepository
import com.google.android.gms.auth.api.signin.GoogleSignInClient

@Suppress("UNCHECKED_CAST")
class HomeViewModelFacotry(
    private val mGoogleSignInClient: GoogleSignInClient,
    private val userRepository: UserRepository,
    private val tripsRepository: TripsRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(userRepository, mGoogleSignInClient, tripsRepository) as T
    }
}