package com.github.skrox.travelally.ui.auth

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.github.skrox.travelally.data.repositories.UserRepository
import com.github.skrox.travelally.util.ActivityNavigation
import com.github.skrox.travelally.util.LiveMessageEvent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task


const val GOOGLE_SIGN_IN=1
const val TAG="Debug"
class AuthViewModel(private val mGoogleSignInClient: GoogleSignInClient,
                    private val userRepository: UserRepository): ViewModel() {


    val startActivityForResultEvent = LiveMessageEvent<ActivityNavigation>()

    fun googleLogin(view: View) {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResultEvent.sendEvent { startActivityForResult(signInIntent, GOOGLE_SIGN_IN) }
    }

    //Called from Activity receving result
    fun onResultFromActivity(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            GOOGLE_SIGN_IN -> {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(task)
            }

        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account =
                completedTask.getResult(ApiException::class.java)

                     Log.e("logged in",account?.idToken.toString())

            // Signed in successfully, show authenticated UI.
//            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            e.printStackTrace()
//            updateUI(null)
        }
    }

}