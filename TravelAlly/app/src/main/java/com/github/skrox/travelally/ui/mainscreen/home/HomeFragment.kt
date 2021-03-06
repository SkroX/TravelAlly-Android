package com.github.skrox.travelally.ui.mainscreen.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.github.skrox.travelally.R
import com.github.skrox.travelally.data.db.entities.Trip
import com.github.skrox.travelally.databinding.FragmentHomeBinding
import com.github.skrox.travelally.ui.mainscreen.MainActivity
import com.github.skrox.travelally.util.snackbar
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject


class HomeFragment() : Fragment(), HomeListener{

    @Inject lateinit var mGoogleSignInClient: GoogleSignInClient
    @Inject lateinit var factory:HomeViewModelFacotry

    private lateinit var mAdapter:GroupAdapter<ViewHolder>

    private val homeViewModel:HomeViewModel by viewModels{factory}

    private lateinit var navController:NavController

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity as MainActivity).mainComponent.inject(this)

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
        binding.viewmodel = homeViewModel
        binding.lifecycleOwner = this
        homeViewModel.homeListener=this

        navController= this.findNavController()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        navController.navigate(R.id.action_nav_home_to_postTripFragment)
        bindUI()
    }

    override fun onFailure(message:String) {
//        root_layout.snackbar(message)
    }

    private fun bindUI(){


        homeViewModel.loadPopularTrips()
        homeViewModel.loadTripsNearMe()
        homeViewModel.loadAllTrips()
        initRecyclerView()
//        context?.let {
//            homeVM.getuser(it).observe(viewLifecycleOwner, Observer {
////                text_home.text=it?.idToken
//            })
//        }

        homeViewModel._popularTrips.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()){
                Log.e("populartrip",it.size.toString())
                addSection(it.toTripItem(), "Popular Trips")
            }
        })

        homeViewModel._tripsNearMe.observe(viewLifecycleOwner, Observer {

            Log.e("nearmetrip", it.size.toString())
            addSection(it.toTripItem(), "Near Me")

        })

        homeViewModel._allTrips.observe(viewLifecycleOwner, Observer {
            Log.e("all trips", it.size.toString())
            addSection(it.toTripItem(), "All Trips")
        })
    }

    private fun List<Trip>.toTripItem(): List<TripItem>{
        return this.map { TripItem(it) }
    }

    private fun initRecyclerView(){

        mAdapter = GroupAdapter<ViewHolder>().apply {
//            addAll(tripItem)
            spanCount=2
        }
        recyclerView.apply {
            layoutManager=GridLayoutManager(this@HomeFragment.context, mAdapter.spanCount).apply {
                spanSizeLookup=mAdapter.spanSizeLookup
            }
            setHasFixedSize(true)
            adapter=mAdapter

        }
    }

    private fun addSection(tripItem: List<TripItem>, name: String){

        val excitingSection = Section()
        Log.e(name,tripItem.size.toString())
        ExpandableGroup(ExpandableHeaderItem(name), true).apply {
            excitingSection.addAll(tripItem)
            add(excitingSection)
            mAdapter.add(this)
        }
    }

}
