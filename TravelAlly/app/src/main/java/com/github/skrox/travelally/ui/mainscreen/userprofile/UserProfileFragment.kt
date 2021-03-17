package com.github.skrox.travelally.ui.mainscreen.userprofile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.skrox.travelally.R
import com.github.skrox.travelally.databinding.UserProfileFragmentBinding
import com.github.skrox.travelally.ui.mainscreen.MainActivity
import javax.inject.Inject

class UserProfileFragment : Fragment(), UserProfileListener {

    @Inject
    lateinit var userProfileVMFactory: UserProfileVMFactory

    private val viewModel: UserProfileViewModel by viewModels { userProfileVMFactory }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity as MainActivity).mainComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding :UserProfileFragmentBinding=
            DataBindingUtil.inflate(inflater, R.layout.user_profile_fragment, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = this
        viewModel.userProfileListener = this
        return binding.root
    }

    override fun onFailure(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

}