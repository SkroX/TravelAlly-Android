package com.github.skrox.travelally.ui.mainscreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.github.skrox.travelally.R
import com.github.skrox.travelally.TravelAllyApplication
import com.github.skrox.travelally.databinding.ActivityMainBinding
import com.github.skrox.travelally.di.MainComponent
import com.github.skrox.travelally.ui.auth.LoginActivity
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.navigation.NavigationView
import de.hdodenhof.circleimageview.CircleImageView
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    @Inject
    lateinit var factory: MainViewModelFactory

    lateinit var navController: NavController

    @Inject
    lateinit var placesClient: PlacesClient

    lateinit var mainComponent: MainComponent

    private val viewModel: MainViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {

        mainComponent = (application as TravelAllyApplication).appComponent.MainComponent().create(
            this
        )
        // Injects this activity to the just created registration component
        mainComponent.inject(this)

        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        val profile_card = findViewById<CardView>(R.id.profile_card)
        profile_card.setBackgroundResource(R.drawable.one_corner_card_bg)

        Log.e("created", "Mainactivity")
//        val toolbar: Toolbar = findViewById(R.id.toolbar)
//        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment)

        val userProfile = findViewById<CircleImageView>(R.id.profile_image)
        userProfile.setOnClickListener {
            navController.navigateUp()
            navController.navigate(R.id.userProfileFragment)
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_gallery,
                R.id.nav_slideshow
            ), drawerLayout
        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        viewModel.getuser(this).observe(this, Observer { user ->
            if (user != null) {
                Log.e("main activity", user.displayName)
                viewModel.user.postValue(user)
            } else {
                Intent(this, LoginActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
