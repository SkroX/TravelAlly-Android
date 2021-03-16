package com.github.skrox.travelally.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.github.skrox.travelally.R
import com.github.skrox.travelally.TravelAllyApplication
import com.github.skrox.travelally.data.repositories.UserRepository
import com.github.skrox.travelally.databinding.ActivityLoginBinding
import com.github.skrox.travelally.di.LoginComponent
import com.github.skrox.travelally.ui.mainscreen.MainActivity
import com.github.skrox.travelally.util.ActivityNavigation
import com.github.skrox.travelally.util.hide
import com.github.skrox.travelally.util.show
import com.github.skrox.travelally.util.snackbar
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject


class LoginActivity : AppCompatActivity(), ActivityNavigation, AuthListener{


    @Inject lateinit var userRepo:UserRepository
    @Inject lateinit var mGoogleSignInClient:GoogleSignInClient
    @Inject lateinit var factory: AuthViewModelFactory

    lateinit var loginComponent:LoginComponent

    private lateinit var vm:AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {

        // Creates an instance of Registration component by grabbing the factory from the app graph
        loginComponent = (application as TravelAllyApplication).appComponent.LoginComponent().create(this)
        // Injects this activity to the just created registration component
        loginComponent.inject(this)

        super.onCreate(savedInstanceState)

//        val gso =
//            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .requestIdToken(resources.getString(R.string.google_client_id))
//                .build()
//
//        val mGoogleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(this, gso);
//        val userReposiotry = UserRepository(MyApi(NetworkConnectionInterceptor(this)), AppDatabase.invoke(this),
//            PreferenceProvider(this)
//        )

        val viewModel: AuthViewModel by viewModels{factory}
        vm = viewModel

        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewmodel = viewModel

        viewModel.authListener=this
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

    override fun onStarted() {
        progress_circular.show()
    }

    override fun onSuccess() {
        progress_circular.hide()

    }

    override fun onFailure(msg: String) {
        progress_circular.hide()
        Log.e("auth error", msg)
        root_layout.snackbar(msg)
    }

}
