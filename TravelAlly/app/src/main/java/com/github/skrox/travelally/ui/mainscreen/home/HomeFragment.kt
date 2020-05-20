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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.skrox.travelally.R
import com.github.skrox.travelally.TravelAllyApplication
import com.github.skrox.travelally.data.db.entities.Trip
import com.github.skrox.travelally.databinding.FragmentHomeBinding
import com.github.skrox.travelally.di.MainComponent
import com.github.skrox.travelally.util.snackbar
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject


class HomeFragment() : Fragment(), HomeListener{

    @Inject lateinit var mGoogleSignInClient: GoogleSignInClient
    @Inject lateinit var factory:HomeViewModelFacotry

    private lateinit var homeVM:HomeViewModel

    private val excitingSection = Section()

    private lateinit var mAdapter:GroupAdapter<ViewHolder>

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
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindUI()
    }

    override fun onFailure(message:String) {
        root_layout.snackbar(message)
    }

    private fun bindUI(){


        homeVM.loadPopularTrips()
        homeVM.loadTripsNearMe()
        homeVM.loadAllTrips()
        initRecyclerView()
        context?.let {
            homeVM.getuser(it).observe(viewLifecycleOwner, Observer {
//                text_home.text=it?.idToken
            })
        }

        homeVM._popularTrips.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()){
                Log.e("populartrip",it.size.toString())
                addSection(it.toTripItem(), "Popular Trips")
            }
        })

        homeVM._tripsNearMe.observe(viewLifecycleOwner, Observer {

            Log.e("nearmetrip", it.size.toString())
            addSection(it.toTripItem(), "Near Me")

        })

        homeVM._allTrips.observe(viewLifecycleOwner, Observer {
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
            spanCount=3
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
        ExpandableGroup(ExpandableHeaderItem(name), true).apply {
            excitingSection.addAll(tripItem)
            add(excitingSection)
            mAdapter.add(this)
        }
    }

}
