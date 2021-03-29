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
import com.github.skrox.travelally.util.Constants.ORDER_NEARME_ITEMS
import com.github.skrox.travelally.util.Constants.ORDER_POPULAR_ITEMS
import com.github.skrox.travelally.util.Constants.VIEW_TYPE_NEARME_ITEMS
import com.github.skrox.travelally.util.Constants.VIEW_TYPE_POPULAR_ITEMS
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import javax.inject.Inject


class HomeFragment : Fragment(), HomeListener {

    @Inject
    lateinit var mGoogleSignInClient: GoogleSignInClient

    @Inject
    lateinit var factory: HomeViewModelFacotry

    private val homeViewModel: HomeViewModel by viewModels { factory }

    private lateinit var navController: NavController

    private val mItemOrderList = mutableListOf<Int>()

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        navController.navigate(R.id.action_nav_home_to_postTripFragment)
        bindUI()
    }

    override fun onFailure(message: String) {
//        root_layout.snackbar(message)
    }

    private fun bindUI() {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.home_rv)
        val glm = StaggeredGridLayoutManager(2, VERTICAL)

        recyclerView?.layoutManager = glm
        val parents: MutableList<ParentModel> = mutableListOf()
        val parentAdapter = ParentAdapter()
        recyclerView?.apply {
            adapter = parentAdapter
        }

        parentAdapter.submitList(parents)
        recyclerView?.setHasFixedSize(true)
//        val helper: SnapHelper = LinearSnapHelper()
//        helper.attachToRecyclerView(recyclerView)


        homeViewModel.loadPopularTrips()
        homeViewModel.loadTripsNearMe()
        homeViewModel.loadAllTrips()

        homeViewModel._popularTrips.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                Log.e("populartrip", it.size.toString())

                val pos = findIndexToInsert(ORDER_POPULAR_ITEMS)
                mItemOrderList.add(pos, ORDER_POPULAR_ITEMS)
                parents.add(pos, ParentModel(VIEW_TYPE_POPULAR_ITEMS, "", it))
                parentAdapter.submitList(parents)
                parentAdapter.notifyDataSetChanged()
            }
        })

        homeViewModel._tripsNearMe.observe(viewLifecycleOwner, Observer {

            Log.e("nearmetrip", it.size.toString())
            val pos = findIndexToInsert(ORDER_NEARME_ITEMS)
            mItemOrderList.add(pos, ORDER_NEARME_ITEMS)
            for (trip in it)
                parents.add(pos, ParentModel(VIEW_TYPE_NEARME_ITEMS, "", mutableListOf(trip)))
            parentAdapter.submitList(parents)
            parentAdapter.notifyDataSetChanged()
        })

        homeViewModel._allTrips.observe(viewLifecycleOwner, Observer {
            Log.e("all trips", it.size.toString())
        })
    }

    private fun findIndexToInsert(order: Int): Int {
        if (order == 0) return 0
        for ((idx, i) in mItemOrderList.withIndex()) {
            if (i >= order) return idx
        }
        return mItemOrderList.size
    }
}
