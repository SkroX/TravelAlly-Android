package com.github.skrox.travelally.ui.mainscreen.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.skrox.travelally.R
import com.github.skrox.travelally.TravelAllyApplication
import com.github.skrox.travelally.databinding.FragmentHomeBinding
import com.github.skrox.travelally.di.MainComponent
import com.github.skrox.travelally.util.snackbar
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject


class HomeFragment() : Fragment(), HomeListener{

    @Inject lateinit var mGoogleSignInClient: GoogleSignInClient
    @Inject lateinit var factory:HomeViewModelFacotry

    private lateinit var homeVM:HomeViewModel

    lateinit var mainComponent:MainComponent
    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainComponent = (requireActivity().application as TravelAllyApplication).appComponent.MainComponent().create(requireActivity())
        // Injects this activity to the just created registration component
        mainComponent.inject(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        Log.e("created","fragment")
//        val gso =
//            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .requestIdToken(resources.getString(R.string.google_client_id))
//                .build()
//        val mGoogleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(activity as MainActivity, gso);

        Log.e("facthome", factory.toString())

        val binding: FragmentHomeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        val homeViewModel:HomeViewModel by viewModels{factory}
        binding.viewmodel = homeViewModel
        binding.lifecycleOwner = this

        homeVM=homeViewModel

        homeViewModel.homeListener=this

        bindUI()

        return binding.root
    }

    override fun onFailure(message:String) {
        root_layout.snackbar(message)
    }

    private fun bindUI(){

        homeVM.loadPopularTrips()
        homeVM.loadTripsNearMe()
        homeVM.loadAllTrips()

        context?.let {
            homeVM.getuser(it).observe(viewLifecycleOwner, Observer {
                text_home.text=it?.idToken
            })
        }

        homeVM._popularTrips.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()){
                Log.e("populartrip",it.size.toString())
            }
        })

        homeVM._tripsNearMe.observe(viewLifecycleOwner, Observer {

                Log.e("nearmetrip", it.size.toString())

        })

        homeVM._allTrips.observe(viewLifecycleOwner, Observer {
            Log.e("all trips", it.size.toString())
        })
    }

}
