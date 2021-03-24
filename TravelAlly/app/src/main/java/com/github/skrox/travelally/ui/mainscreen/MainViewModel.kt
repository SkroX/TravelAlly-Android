package com.github.skrox.travelally.ui.mainscreen

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.skrox.travelally.data.repositories.UserRepository
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient

class MainViewModel(
    private val mGoogleSignInClient: GoogleSignInClient,
    private val userRepo: UserRepository
) : ViewModel() {

    var user: MutableLiveData<GoogleSignInAccount?> = MutableLiveData()
    fun getuser(context: Context)=userRepo.getUser(context)

}