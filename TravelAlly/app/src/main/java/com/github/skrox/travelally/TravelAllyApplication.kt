package com.github.skrox.travelally


import android.app.Application
import com.github.skrox.travelally.di.AppComponent
import com.github.skrox.travelally.di.DaggerAppComponent

open class TravelAllyApplication : Application() {

    // Instance of the AppComponent that will be used by all the Activities in the project
    val appComponent: AppComponent by lazy {
        // Creates an instance of AppComponent using its Factory constructor
        // We pass the applicationContext that will be used as Context in the graph
        DaggerAppComponent.factory().create(applicationContext)
    }
}