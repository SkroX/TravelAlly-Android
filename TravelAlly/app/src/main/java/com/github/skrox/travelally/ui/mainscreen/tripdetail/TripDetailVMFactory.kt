package com.github.skrox.travelally.ui.mainscreen.tripdetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.skrox.travelally.data.repositories.TripsRepository
import com.github.skrox.travelally.data.repositories.UserRepository
import com.github.skrox.travelally.di.ActivityScope
import com.github.skrox.travelally.ui.mainscreen.home.HomeViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import javax.inject.Inject

@ActivityScope
@Suppress("UNCHECKED_CAST")
class TripDetailVMFactory @Inject constructor(
    private val tripsRepository: TripsRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TripDetailViewModel(tripsRepository) as T
    }
}