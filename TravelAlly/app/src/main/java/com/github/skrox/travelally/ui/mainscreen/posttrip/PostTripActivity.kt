package com.github.skrox.travelally.ui.mainscreen.posttrip

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.ui.setupActionBarWithNavController
import com.aceinteract.android.stepper.StepperNavListener
import com.aceinteract.android.stepper.StepperNavigationView
import com.github.skrox.travelally.R
import com.github.skrox.travelally.TravelAllyApplication
import com.github.skrox.travelally.di.PostTripComponent
import com.github.skrox.travelally.ui.mainscreen.posttrip.temp.PostTripViewModelFacotry
import com.github.skrox.travelally.util.findNavControllerFromFragmentContainer
import com.github.skrox.travelally.util.hide
import com.github.skrox.travelally.util.show
import com.github.skrox.travelally.util.snackbar
import com.google.android.material.button.MaterialButton
import com.orhanobut.dialogplus.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_post_trip.*
import kotlinx.android.synthetic.main.activity_post_trip.progress_circular
import kotlinx.android.synthetic.main.activity_post_trip.root_layout
import javax.inject.Inject


class PostTripActivity : AppCompatActivity(), StepperNavListener, PostTripListener {

    lateinit var postTripComponent: PostTripComponent

    @Inject
    lateinit var factory: PostTripViewModelFacotry
    val postTripViewModel: PostTripViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {

        postTripComponent =
            (application as TravelAllyApplication).appComponent.PostTripComponent().create(
                this
            )
        // Injects this activity to the just created registration component
        postTripComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_trip)

        stepper.initializeStepper()

        setSupportActionBar(toolbar)

        // Setup Action bar for title and up navigation.
        setupActionBarWithNavController(findNavControllerFromFragmentContainer(R.id.frame_stepper))

        button_next.setOnClickListener {
            stepper.goToNextStep()
        }
        postTripViewModel.getPostFields()!!.observe(this, Observer<Any> { ptm ->
            Toast.makeText(
                this,
                "Email " + ptm,
                Toast.LENGTH_SHORT
            ).show()
        })
        collectStateFlow()

        postTripViewModel.postTripListener = this
    }

    private fun StepperNavigationView.initializeStepper() {
//        viewModel.updateStepper(
//            StepperSettings(
//                widgetColor,
//                textColor,
//                textSize,
//                iconSize
//            )
//        )

        stepperNavListener = this@PostTripActivity
        setupWithNavController(findNavControllerFromFragmentContainer(R.id.frame_stepper))
    }

    private fun collectStateFlow() {
//        viewModel.stepperSettings.onEach {
//            stepper.widgetColor = it.iconColor
//            stepper.textColor = it.textColor
//            stepper.textSize = it.textSize
//            stepper.iconSize = it.iconSize
//        }.launchIn(lifecycleScope)
    }

    override fun onStepChanged(step: Int) {
//        showToast("Step changed to: $step")
        Toast.makeText(this, "step " + step, Toast.LENGTH_SHORT).show()

        if (step == 2) {
            button_next.setImageResource(R.drawable.ic_check)
        } else {
            button_next.setImageResource(R.drawable.ic_right)
        }
    }

    override fun onCompleted() {

//        Toast.makeText(this, "stepper comp", Toast.LENGTH_SHORT).show()
//        showToast("Stepper completed")
        Log.e("post trip", "" + postTripViewModel.getPostFields()?.value)
        postTripViewModel.onButtonClick()
        Toast.makeText(
            this,
            "Email " + postTripViewModel.getPostFields(),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showCompleteDialog() {
        val holder: Holder
        holder = ViewHolder(R.layout.dialog_content)
        val builder = DialogPlus.newDialog(this).apply {
            setContentHolder(holder)
            setGravity(Gravity.BOTTOM)
            setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
            overlayBackgroundResource = R.color.transparent_overlay
            isCancelable = false
            onDismissListener = OnDismissListener { }
            contentBackgroundResource = R.color.colorPrimary
            contentBackgroundResource = R.drawable.corner
            //                .setOutMostMargin(0, 100, 0, 0)

            setOnClickListener { dialog, view ->
                if (view is MaterialButton) {
                    Toast.makeText(this.context, "yes", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            setOnBackPressListener {
                finish()
            }
        }

        builder.create().show()
    }

    /**
     * Use navigation controller to navigate up.
     */
    override fun onSupportNavigateUp(): Boolean =
        findNavControllerFromFragmentContainer(R.id.frame_stepper).navigateUp()

    /**
     * Navigate up when the back button is pressed.
     */
    override fun onBackPressed() {
        if (stepper.currentStep == 0) {
            super.onBackPressed()
        } else {
            findNavControllerFromFragmentContainer(R.id.frame_stepper).navigateUp()
        }
    }

    override fun onStarted() {
        progress_circular.show()
        Log.e("post trip", " started")
    }

    override fun onSuccess() {
        Log.e("post trip", "successs")
        progress_circular.hide()

        showCompleteDialog()
    }

    override fun onFailure(msg: String) {
        progress_circular.hide()

        root_layout.snackbar(msg)
    }
}
