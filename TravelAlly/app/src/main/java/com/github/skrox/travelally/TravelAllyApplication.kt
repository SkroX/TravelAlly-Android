package com.github.skrox.travelally

import android.app.Application
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule

class TravelAllyApplication : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@TravelAllyApplication))

//        bind() from singleton { NetworkConnectionInterceptor(instance()) }
//        bind() from singleton { MyApi(instance()) }
//        bind() from singleton { AppDatabase(instance()) }
//        bind() from singleton { PreferenceProvider(instance()) }
//        bind() from singleton { UserRepository() }
//        bind() from singleton { QuotesRepository(instance(), instance(), instance()) }
//        bind() from provider { AuthViewModelFactory(instance(), instance()) }
//        bind() from provider { ProfileViewModelFactory(instance()) }
//        bind() from provider{ QuotesViewModelFactory(instance())}


    }

}