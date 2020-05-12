package com.github.skrox.travelally.ui.mainscreen.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.github.skrox.travelally.R
import com.github.skrox.travelally.data.repositories.UserRepository
import com.github.skrox.travelally.databinding.ActivityLoginBinding
import com.github.skrox.travelally.databinding.FragmentHomeBinding
import com.github.skrox.travelally.ui.auth.AuthViewModel
import com.github.skrox.travelally.ui.auth.AuthViewModelFactory
import com.github.skrox.travelally.ui.auth.LoginActivity
import com.github.skrox.travelally.ui.mainscreen.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import kotlinx.android.synthetic.main.fragment_home.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class HomeFragment() : Fragment(), KodeinAware {

    override val kodein: Kodein by kodein()

    private val userRepo:UserRepository by instance<UserRepository>()

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        Log.e("created","fragment")
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(resources.getString(R.string.google_client_id))
                .build()

        val mGoogleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(activity as MainActivity, gso);
        val factory= HomeViewModelFacotry(mGoogleSignInClient, userRepo)
        val binding: FragmentHomeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        val homeViewModel:HomeViewModel by viewModels{factory}
        binding.viewmodel = homeViewModel
        binding.lifecycleOwner = this

//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            text_home.text = it
//        })

//        var string:String?
//        context?.let {
//            homeViewModel.userRepository.getUser(it).observe(viewLifecycleOwner, Observer<GoogleSignInAccount?>{
//                val s:String? = it?.idToken ?: "null"
//                Log.e("logged out",s)
//
//            })
//        }

        context?.let {
            homeViewModel.getuser(it).observe(viewLifecycleOwner, Observer {
                text_home.text=it?.idToken
            })
        }


        return binding.root
    }


}
