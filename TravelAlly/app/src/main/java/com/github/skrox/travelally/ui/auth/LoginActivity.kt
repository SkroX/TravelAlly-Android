package com.github.skrox.travelally.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.github.skrox.travelally.R
import com.github.skrox.travelally.data.db.AppDatabase
import com.github.skrox.travelally.data.db.entities.User
import com.github.skrox.travelally.data.network.MyApi
import com.github.skrox.travelally.data.network.NetworkConnectionInterceptor
import com.github.skrox.travelally.data.preferences.PreferenceProvider
import com.github.skrox.travelally.data.repositories.UserRepository
import com.github.skrox.travelally.databinding.ActivityLoginBinding
import com.github.skrox.travelally.ui.mainscreen.MainActivity
import com.github.skrox.travelally.util.ActivityNavigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class LoginActivity : AppCompatActivity(), ActivityNavigation, KodeinAware{

    override val kodein by kodein()
    private val userRepo:UserRepository by instance<UserRepository>()

    private lateinit var vm:AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(resources.getString(R.string.google_client_id))
                .build()

        val mGoogleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(this, gso);
//        val userReposiotry = UserRepository(MyApi(NetworkConnectionInterceptor(this)), AppDatabase.invoke(this),
//            PreferenceProvider(this)
//        )
        val factory=AuthViewModelFactory(mGoogleSignInClient, userRepo)
        val viewModel: AuthViewModel by viewModels{factory}
        vm = viewModel

        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewmodel = viewModel
//        viewModel.updateuser((GoogleSignIn.getLastSignedInAccount(this)));

        viewModel.getuser(this).observe(this, Observer { user->
            if (user!=null){
                Intent(this, MainActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                }
            }
        })
//        Intent(this,MainActivity::class.java).also { startActivity(it) }
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
