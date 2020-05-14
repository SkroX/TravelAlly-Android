package com.github.skrox.travelally


import android.app.Application
import com.github.skrox.travelally.data.db.AppDatabase
import com.github.skrox.travelally.data.network.MyApi
import com.github.skrox.travelally.data.network.NetworkConnectionInterceptor
import com.github.skrox.travelally.data.preferences.PreferenceProvider
import com.github.skrox.travelally.data.repositories.TripsRepository
import com.github.skrox.travelally.data.repositories.UserRepository
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class TravelAllyApplication : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@TravelAllyApplication))

        bind() from singleton { PreferenceProvider(instance()) }
        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { MyApi(instance()) }
        bind() from singleton { AppDatabase(instance()) }
        bind() from singleton { UserRepository(instance(),instance(),instance()) }
        bind() from singleton { TripsRepository(instance(),instance(), instance()) }
//        bind() from singleton { QuotesRepository(instance(), instance(), instance()) }
//        bind() from provider { AuthViewModelFactory(instance(), instance()) }
//        bind() from provider { ProfileViewModelFactory(instance()) }
//        bind() from provider{ QuotesViewModelFactory(instance())}


    }

}