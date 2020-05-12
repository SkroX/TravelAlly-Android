package com.github.skrox.travelally.ui.mainscreen.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.skrox.travelally.data.repositories.UserRepository
import com.google.android.gms.auth.api.signin.GoogleSignInClient

@Suppress("UNCHECKED_CAST")
class HomeViewModelFacotry(
    private val mGoogleSignInClient: GoogleSignInClient,
    private val repository: UserRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(repository, mGoogleSignInClient) as T
    }
}