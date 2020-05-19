package com.github.skrox.travelally


import android.app.Application
import com.github.skrox.travelally.di.AppComponent
import com.github.skrox.travelally.di.DaggerAppComponent

open class TravelAllyApplication : Application(){

    // Instance of the AppComponent that will be used by all the Activities in the project
    val appComponent: AppComponent by lazy {
        // Creates an instance of AppComponent using its Factory constructor
        // We pass the applicationContext that will be used as Context in the graph
        DaggerAppComponent.factory().create(applicationContext)
    }


//    override val kodein = Kodein.lazy {
//        import(androidXModule(this@TravelAllyApplication))
//
//        bind() from singleton { PreferenceProvider(instance()) }
//        bind() from singleton { NetworkConnectionInterceptor(instance()) }
//        bind() from singleton { MyApi(instance()) }
//        bind() from singleton { AppDatabase(instance()) }
//        bind() from singleton { UserRepository(instance(),instance()) }
//        bind() from singleton { TripsRepository(instance(),instance()) }
//        bind() from provider { AuthViewModelFactory(instance(), instance()) }
////        bind() from singleton { QuotesRepository(instance(), instance(), instance()) }
////        bind() from provider { ProfileViewModelFactory(instance()) }
////        bind() from provider{ QuotesViewModelFactory(instance())}
//
//
//    }
}

//class TravelAllyApplication : Application(), KodeinAware{
//
////    override val kodein = Kodein.lazy {
////        import(androidXModule(this@TravelAllyApplication))
////
////        bind() from singleton { PreferenceProvider(instance()) }
////        bind() from singleton { NetworkConnectionInterceptor(instance()) }
////        bind() from singleton { MyApi(instance()) }
////        bind() from singleton { AppDatabase(instance()) }
////        bind() from singleton { UserRepository(instance(),instance(),instance()) }
////        bind() from singleton { TripsRepository(instance(),instance(), instance()) }
//////        bind() from singleton { QuotesRepository(instance(), instance(), instance()) }
//////        bind() from provider { AuthViewModelFactory(instance(), instance()) }
//////        bind() from provider { ProfileViewModelFactory(instance()) }
//////        bind() from provider{ QuotesViewModelFactory(instance())}
////
////
////    }
//
//}