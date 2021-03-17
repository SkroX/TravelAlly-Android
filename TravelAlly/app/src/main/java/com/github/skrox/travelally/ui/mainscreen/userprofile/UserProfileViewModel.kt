package com.github.skrox.travelally.ui.mainscreen.userprofile

import androidx.lifecycle.ViewModel
import com.github.skrox.travelally.data.repositories.TripsRepository
import com.github.skrox.travelally.data.repositories.UserRepository

class UserProfileViewModel(
    private val tripsRepository: TripsRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    var userProfileListener: UserProfileListener? = null
}