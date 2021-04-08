package com.github.skrox.travelally.ui.mainscreen.userprofile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.skrox.travelally.R
import com.github.skrox.travelally.databinding.UserProfileFragmentBinding
import com.github.skrox.travelally.ui.mainscreen.MainActivity
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.button.MaterialButton
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.Holder
import com.orhanobut.dialogplus.ViewHolder
import kotlinx.android.synthetic.main.radius_dialog.view.*
import kotlinx.android.synthetic.main.user_profile_fragment.*
import javax.inject.Inject

class UserProfileFragment : Fragment(), UserProfileListener {

    @Inject
    lateinit var userProfileVMFactory: UserProfileVMFactory

    private val viewModel: UserProfileViewModel by viewModels { userProfileVMFactory }
    private val AUTOCOMPLETE_REQUEST_CODE = 1

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity as MainActivity).mainComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: UserProfileFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.user_profile_fragment, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = this
        viewModel.userProfileListener = this

        viewModel.getUser(this.requireContext()).observe(viewLifecycleOwner, Observer { user ->
            if (user != null) {
                viewModel.user.postValue(user)
            }
        })
        activity?.findViewById<CardView>(R.id.profile_card)?.visibility = GONE
        return binding.root
    }

    override fun onFailure(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        radius.setOnClickListener {
            showRadiusDialog()
        }
        location.setOnClickListener {
            placesAutocomplete()
        }
        radius_ll.setOnClickListener {
            showRadiusDialog()
        }
    }

    private fun showRadiusDialog() {
        val holder: Holder
        holder = ViewHolder(R.layout.radius_dialog)
        val builder = DialogPlus.newDialog(context).apply {
            setContentHolder(holder)
            setGravity(Gravity.CENTER)
            setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
            overlayBackgroundResource = R.color.transparent_overlay
            contentBackgroundResource = R.color.colorPrimary
            contentBackgroundResource = R.drawable.corner

            setOnClickListener { dialog, view ->
                if (view is MaterialButton) {
                    Toast.makeText(
                        this.context,
                        holder.inflatedView.radius_tv.text,
                        Toast.LENGTH_SHORT
                    ).show()

                    if (holder.inflatedView.radius_tv.text.toString().isNotEmpty()) {
                        viewModel.radius.postValue(holder.inflatedView.radius_tv.text.toString())
                        viewModel.setRadius(holder.inflatedView.radius_tv.text.toString())
                    }

                    dialog.dismiss()
                }
            }
        }

        builder.create().show()
    }

    fun placesAutocomplete() {
        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        val fields = listOf(
            Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG
        )

        // Start the autocomplete intent.
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
            .build(requireContext())
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        viewModel.location.postValue(place.name)
                        place.latLng?.let { it1 -> viewModel.setLat(it1.latitude) }
                        place.latLng?.let { it1 -> viewModel.setLon(it1.longitude) }
                        place.name?.let { it1 -> viewModel.setLocName(it1) }

//                        Log.i(TAG, "Place: ${place.name}, ${place.id}")
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    // TODO: Handle the error.
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Log.e("user prof autocomp", status.toString())
//                        Log.i(TAG, status.statusMessage)
                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


}