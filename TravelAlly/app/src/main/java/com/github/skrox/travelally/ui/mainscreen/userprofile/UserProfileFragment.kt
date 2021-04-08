package com.github.skrox.travelally.ui.mainscreen.userprofile

import android.content.Context
import android.os.Bundle
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

}