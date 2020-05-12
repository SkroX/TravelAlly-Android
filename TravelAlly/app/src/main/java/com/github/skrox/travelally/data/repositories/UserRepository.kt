package com.github.skrox.travelally.data.repositories

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.skrox.travelally.data.db.AppDatabase
import com.github.skrox.travelally.data.network.MyApi
import com.github.skrox.travelally.data.network.SafeApiRequest
import com.github.skrox.travelally.data.network.postobjects.SendToken
import com.github.skrox.travelally.data.network.responses.AuthResponse
import com.github.skrox.travelally.data.preferences.PreferenceProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.OnCompleteListener


class UserRepository(private val api:MyApi,
                    private val db:AppDatabase,
                    private val prefs: PreferenceProvider
) : SafeApiRequest() {

    private val user = MutableLiveData<GoogleSignInAccount?>()

    suspend fun login(id_info:String?): AuthResponse{
        return apiRequest {api.userLogin(
            SendToken(
                id_info
            )
        )}
    }

    fun saveuser(authResponse: AuthResponse, account: GoogleSignInAccount?){
//        db.getUserDao().upsert(authResponse.user)
        prefs.saveToken(authResponse.token)
        user.postValue(account)
    }

    fun getUser(context: Context): LiveData<GoogleSignInAccount?>{

        if(user.value!=GoogleSignIn.getLastSignedInAccount(context))
        user.postValue(GoogleSignIn.getLastSignedInAccount(context))
        return user
    }

    fun getToken()=prefs.getToken()

    fun logout(){
        user.postValue(null)
        Log.e("logged out","success")
    }
}