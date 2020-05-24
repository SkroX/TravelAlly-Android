package com.github.skrox.travelally.ui.mainscreen.tripdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.skrox.travelally.data.repositories.TripsRepository
import com.github.skrox.travelally.di.ActivityScope
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