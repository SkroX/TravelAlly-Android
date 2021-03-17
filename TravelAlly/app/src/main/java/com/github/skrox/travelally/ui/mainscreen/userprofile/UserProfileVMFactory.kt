package com.github.skrox.travelally.ui.mainscreen.userprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.skrox.travelally.data.repositories.TripsRepository
import com.github.skrox.travelally.data.repositories.UserRepository
import com.github.skrox.travelally.di.ActivityScope
import javax.inject.Inject

@ActivityScope
@Suppress("UNCHECKED_CAST")
class UserProfileVMFactory @Inject constructor(
    private val tripsRepository: TripsRepository,
    private val userRepository: UserRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserProfileViewModel(tripsRepository, userRepository) as T
    }
}