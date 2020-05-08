package com.github.skrox.travelally.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.github.skrox.travelally.R
import com.github.skrox.travelally.data.repositories.UserRepository
import com.github.skrox.travelally.databinding.ActivityLoginBinding
import com.github.skrox.travelally.util.ActivityNavigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions



class LoginActivity : AppCompatActivity(), ActivityNavigation {

    private lateinit var vm:AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("728511331909-sqhmkvk3s46j0u86vp8k0ro4jhbog6rs.apps.googleusercontent.com")
                .build()

        val mGoogleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(this, gso);
        val userResposiotry = UserRepository()
        val factory=AuthViewModelFactory(mGoogleSignInClient, userResposiotry)
        val viewModel: AuthViewModel by viewModels{factory}
        vm = viewModel

        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewmodel = viewModel

        subscribeUi()

    }


    //Called from onCreate once the ViewModel is initialized.
    private fun subscribeUi() {
        //this sets the LifeCycler owner and receiver
        vm.startActivityForResultEvent.setEventReceiver(this, this)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        vm.onResultFromActivity(requestCode,resultCode,data)
        super.onActivityResult(requestCode, resultCode, data)
    }

}
