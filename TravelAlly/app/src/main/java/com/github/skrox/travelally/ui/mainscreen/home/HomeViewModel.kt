package com.github.skrox.travelally.ui.mainscreen.home

import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.skrox.travelally.data.repositories.UserRepository
import com.github.skrox.travelally.ui.mainscreen.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.OnCompleteListener


class HomeViewModel( private val userRepository: UserRepository, private val mGoogleSignInClient: GoogleSignInClient) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    fun logout(view:View){
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(view.context as MainActivity, OnCompleteListener<Void?> {
                // ...
                userRepository.logout()
            })

    }

    fun getuser(context:Context)=userRepository.getUser(context)


}