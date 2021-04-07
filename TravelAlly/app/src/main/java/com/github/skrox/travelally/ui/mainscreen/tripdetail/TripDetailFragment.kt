package com.github.skrox.travelally.ui.mainscreen.tripdetail

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.skrox.travelally.R
import com.github.skrox.travelally.databinding.TripDetailFragmentBinding
import com.github.skrox.travelally.ui.mainscreen.MainActivity
import javax.inject.Inject

class TripDetailFragment : Fragment(), TripDetailListener {

    @Inject
    lateinit var tripDetailVMFactory: TripDetailVMFactory

    private var tripId: Int? = null
    private val viewModel: TripDetailViewModel by viewModels { tripDetailVMFactory }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity as MainActivity).mainComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tripId = arguments?.getInt("tripId")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: TripDetailFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.trip_detail_fragment, container, false)
        binding.trip = viewModel
        binding.lifecycleOwner = this
        viewModel.tripDetailListener = this
        viewModel.tripId.postValue(tripId)
        activity?.findViewById<CardView>(R.id.profile_card)?.visibility = View.GONE
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.e("tripdetailid", tripId.toString())

    }

    override fun onFailure(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
    }

}
