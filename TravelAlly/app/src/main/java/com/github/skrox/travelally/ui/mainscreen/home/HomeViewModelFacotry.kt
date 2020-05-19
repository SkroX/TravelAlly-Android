package com.github.skrox.travelally.ui.mainscreen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.skrox.travelally.data.repositories.TripsRepository
import com.github.skrox.travelally.data.repositories.UserRepository
import com.github.skrox.travelally.di.ActivityScope
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import javax.inject.Inject

@ActivityScope
@Suppress("UNCHECKED_CAST")
class HomeViewModelFacotry @Inject constructor(
    private val mGoogleSignInClient: GoogleSignInClient,
    private val userRepository: UserRepository,
    private val tripsRepository: TripsRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        Log.e("homevmfac", userRepository.toString())
        return HomeViewModel(userRepository, mGoogleSignInClient, tripsRepository) as T
    }
}