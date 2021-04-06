package com.github.skrox.travelally.ui.mainscreen.posttrip.stepfragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.github.skrox.travelally.R
import com.github.skrox.travelally.databinding.FragmentStep1Binding
import com.github.skrox.travelally.ui.mainscreen.posttrip.DatePickerFragment
import com.github.skrox.travelally.ui.mainscreen.posttrip.PostTripActivity
import com.github.skrox.travelally.ui.mainscreen.posttrip.PostTripViewModel
import com.github.skrox.travelally.ui.mainscreen.posttrip.temp.PostTripViewModelFacotry
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import javax.inject.Inject

class Step1Fragment : Fragment() {

    @Inject
    lateinit var placesClient: PlacesClient

    private lateinit var postTripViewModel: PostTripViewModel

    private val REQUEST_CODE_START = 2
    private val REQUEST_CODE_END = 3

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        postTripViewModel  = (activity as PostTripActivity).postTripViewModel
        val binding: FragmentStep1Binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_step1, container, false)
        binding.viewmodel = postTripViewModel
        binding.lifecycleOwner = this
        binding.fragment = this
        (activity as PostTripActivity).postTripViewModel
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as PostTripActivity).postTripComponent.inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setPlaceListeners()

    }

    fun showDatePickerDialog(v: View) {
        // get fragment manager so we can launch from fragment
        val fm: FragmentManager = parentFragmentManager
        // create the datePickerFragment
        val newFragment = DatePickerFragment();
        if (v.id == R.id.start_date_tv || v.id == R.id.start_date)
            newFragment.setTargetFragment(this, REQUEST_CODE_START);
        else
            newFragment.setTargetFragment(this, REQUEST_CODE_END);
        // show the datePicker
        fm.let { newFragment.show(it, "datePicker") }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // check for the results
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_END || requestCode == REQUEST_CODE_START) {
            // get date from string
            val date = data?.getStringExtra("selectedDate")
            // set the value of the editText

            if (requestCode == REQUEST_CODE_START)
                postTripViewModel.liveStartDate.postValue(date)
            else
                postTripViewModel.liveEndDate.postValue(date)
        } else {
            val status = Autocomplete.getStatusFromIntent(data!!)
            Log.e("gpi", status.toString())
        }
    }

    private fun setPlaceListeners() {
        // Initialize the AutocompleteSupportFragment.
        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment: AutocompleteSupportFragment =
            childFragmentManager.findFragmentById(R.id.start_place) as AutocompleteSupportFragment

        // Specify the types of place data to return.

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(
            Arrays.asList(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG
            )
        )

        // Set up a PlaceSelectionListener to handle the response.

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                Log.e(
                    "place selected",
                    "Place name " + place.name + " Place latlng " + place.latLng
                )
                val df = DecimalFormat("#.######")
                df.roundingMode = RoundingMode.HALF_DOWN

                postTripViewModel.startLat = df.format(place.latLng!!.latitude).toDouble()
                postTripViewModel.startLon = df.format(place.latLng!!.longitude).toDouble()
                postTripViewModel.startName = place.name!!
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                //                Log.i(TAG, "An error occurred: $status")

                Log.e("places error", status.toString())
            }
        })


        val autocompleteFragmentend: AutocompleteSupportFragment =
            childFragmentManager.findFragmentById(R.id.end_place) as AutocompleteSupportFragment

        // Specify the types of place data to return.

        // Specify the types of place data to return.
        autocompleteFragmentend.setPlaceFields(
            Arrays.asList(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG
            )
        )

        // Set up a PlaceSelectionListener to handle the response.

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragmentend.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                val df = DecimalFormat("#.######")
                df.roundingMode = RoundingMode.HALF_DOWN
                postTripViewModel.endLat = df.format(place.latLng!!.latitude).toDouble()
                postTripViewModel.endLon = df.format(place.latLng!!.longitude).toDouble()
                postTripViewModel.destName = place.name!!
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                //                Log.i(TAG, "An error occurred: $status")
            }
        })


    }
}