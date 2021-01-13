package com.github.skrox.travelally.ui.mainscreen.posttrip.temp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.skrox.travelally.data.repositories.TripsRepository
import com.github.skrox.travelally.ui.mainscreen.posttrip.PostTripViewModel
import javax.inject.Inject
import javax.inject.Singleton
//TODO singleton ni hoga
@Singleton
@Suppress("UNCHECKED_CAST")
class PostTripViewModelFacotry @Inject constructor(
    private val tripsRepository: TripsRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PostTripViewModel(tripsRepository) as T
    }
}