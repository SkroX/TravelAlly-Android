package com.github.skrox.travelally.ui.mainscreen.posttrip.stepfragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.skrox.travelally.R
import com.github.skrox.travelally.databinding.FragmentStep2Binding
import com.github.skrox.travelally.ui.mainscreen.posttrip.PostTripActivity
import com.github.skrox.travelally.ui.mainscreen.posttrip.PostTripViewModel
import com.github.skrox.travelally.ui.mainscreen.posttrip.temp.PostTripViewModelFacotry
import javax.inject.Inject

class Step2Fragment : Fragment() {

    private lateinit var postTripViewModel: PostTripViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        postTripViewModel  = (activity as PostTripActivity).postTripViewModel

        // Inflate the layout for this fragment
        val binding: FragmentStep2Binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_step2, container, false)
        binding.viewmodel = postTripViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as PostTripActivity).postTripComponent.inject(this)
    }
}