package com.github.skrox.travelally.ui.mainscreen.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
import com.github.skrox.travelally.R
import com.github.skrox.travelally.data.db.entities.ParentModel
import com.github.skrox.travelally.databinding.FragmentHomeBinding
import com.github.skrox.travelally.ui.mainscreen.MainActivity
import com.github.skrox.travelally.ui.mainscreen.home.adapters.ParentAdapter
import com.github.skrox.travelally.util.Constants.ORDER_ALLTRIPS_HEADING
import com.github.skrox.travelally.util.Constants.ORDER_ALLTRIPS_ITEMS
import com.github.skrox.travelally.util.Constants.ORDER_NEARME_HEADING
import com.github.skrox.travelally.util.Constants.ORDER_NEARME_ITEMS
import com.github.skrox.travelally.util.Constants.ORDER_POPULAR_HEADING
import com.github.skrox.travelally.util.Constants.ORDER_POPULAR_ITEMS
import com.github.skrox.travelally.util.Constants.VIEW_TYPE_ALLTRIPS_HEADING
import com.github.skrox.travelally.util.Constants.VIEW_TYPE_ALLTRIPS_ITEMS
import com.github.skrox.travelally.util.Constants.VIEW_TYPE_NEARME_HEADING
import com.github.skrox.travelally.util.Constants.VIEW_TYPE_NEARME_ITEMS
import com.github.skrox.travelally.util.Constants.VIEW_TYPE_POPULAR_HEADING
import com.github.skrox.travelally.util.Constants.VIEW_TYPE_POPULAR_ITEMS
import com.github.skrox.travelally.util.snackbar
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject


class HomeFragment : Fragment(), HomeListener {

    @Inject
    lateinit var mGoogleSignInClient: GoogleSignInClient

    @Inject
    lateinit var factory: HomeViewModelFacotry

    private val homeViewModel: HomeViewModel by viewModels { factory }

    private lateinit var navController: NavController

    private val mItemOrderList = mutableListOf<Int>()
    private val parents: MutableList<ParentModel> = mutableListOf()
    private val parentAdapter = ParentAdapter()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity as MainActivity).mainComponent.inject(this)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: FragmentHomeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.viewmodel = homeViewModel
        binding.lifecycleOwner = this
        homeViewModel.homeListener = this
        navController = this.findNavController()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab.setOnClickListener {
            navController.navigate(R.id.postTripActivity)
        }
        parents.clear()
        mItemOrderList.clear()
        bindUI()
    }


    override fun onFailure(message: String) {
        root_layout.snackbar(message)
    }

    private fun bindUI() {

        homeViewModel.loadPopularTrips()
        homeViewModel.loadTripsNearMe()
        homeViewModel.loadAllTrips()

        initRecyclerView()

        homeViewModel._popularTrips.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                Log.e("populartrip", it.size.toString())
                addToParentList(ParentModel(VIEW_TYPE_POPULAR_ITEMS, "", it), ORDER_POPULAR_ITEMS)
            }
        })

        homeViewModel._tripsNearMe.observe(viewLifecycleOwner, Observer {
            Log.e("nearmetrip", it.size.toString())
            for (trip in it)
                addToParentList(
                    ParentModel(VIEW_TYPE_NEARME_ITEMS, "", mutableListOf(trip)),
                    ORDER_NEARME_ITEMS
                )
        })

        homeViewModel._allTrips.observe(viewLifecycleOwner, Observer {
            Log.e("all trips", it.size.toString())
            for (trip in it)
                addToParentList(
                    ParentModel(VIEW_TYPE_ALLTRIPS_ITEMS, "", mutableListOf(trip)),
                    ORDER_ALLTRIPS_ITEMS
                )
        })
    }

    private fun findIndexToInsert(order: Int): Int {
        if (order == 0) return 0
        for ((idx, i) in mItemOrderList.withIndex()) {
            if (i >= order) return idx
        }
        return mItemOrderList.size
    }

    private fun initRecyclerView() {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.home_rv)
        val glm = StaggeredGridLayoutManager(2, VERTICAL)
        recyclerView?.layoutManager = glm
        recyclerView?.apply {
            adapter = parentAdapter
            setHasFixedSize(true)
        }

//        val helper: SnapHelper = LinearSnapHelper()
//        helper.attachToRecyclerView(recyclerView)

        addToParentList(
            ParentModel(VIEW_TYPE_POPULAR_HEADING, "Popular Trips", mutableListOf()),
            ORDER_POPULAR_HEADING
        )
        addToParentList(
            ParentModel(VIEW_TYPE_NEARME_HEADING, "Trips Near You", mutableListOf()),
            ORDER_NEARME_HEADING
        )
        addToParentList(
            ParentModel(VIEW_TYPE_ALLTRIPS_HEADING, "All Trips", mutableListOf()),
            ORDER_ALLTRIPS_HEADING
        )
    }

    private fun addToParentList(par: ParentModel, order: Int) {
        val pos = findIndexToInsert(order)
        mItemOrderList.add(pos, order)
        parents.add(pos, par)
        parentAdapter.submitList(parents)
        parentAdapter.notifyDataSetChanged()
    }

    override fun onStop() {
        super.onStop()
        activity?.findViewById<CardView>(R.id.profile_card)?.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<CardView>(R.id.profile_card)?.visibility = View.VISIBLE

    }

}
