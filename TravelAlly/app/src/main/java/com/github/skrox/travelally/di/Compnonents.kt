package com.github.skrox.travelally.di

import android.app.Activity
import android.content.Context
import com.github.skrox.travelally.ui.auth.LoginActivity
import com.github.skrox.travelally.ui.mainscreen.MainActivity
import com.github.skrox.travelally.ui.mainscreen.home.HomeFragment
import com.github.skrox.travelally.ui.mainscreen.posttrip.PostTripActivity
import com.github.skrox.travelally.ui.mainscreen.posttrip.stepfragments.Step1Fragment
import com.github.skrox.travelally.ui.mainscreen.posttrip.stepfragments.Step2Fragment
import com.github.skrox.travelally.ui.mainscreen.posttrip.stepfragments.Step3Fragment
import com.github.skrox.travelally.ui.mainscreen.tripdetail.TripDetailFragment
import com.github.skrox.travelally.ui.mainscreen.userprofile.UserProfileFragment
import dagger.BindsInstance
import dagger.Component
import dagger.Subcomponent
import javax.inject.Singleton

@Singleton
@Component(modules = [UtilsModule::class, AppSubcomponents::class, PlacesAPiModule::class])
interface AppComponent {

    // Factory to create instances of the AppComponent
    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        fun create(@BindsInstance context: Context): AppComponent
    }

    // Expose RegistrationComponent factory from the graph
    fun LoginComponent(): LoginComponent.Factory

    fun MainComponent(): MainComponent.Factory

    fun PostTripComponent(): PostTripComponent.Factory


}

@ActivityScope
@Subcomponent(modules = [LoginModule::class])
interface LoginComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance activity: Activity): LoginComponent
    }

    fun inject(activity: LoginActivity)

}

@ActivityScope
@Subcomponent(modules = [LoginModule::class])
interface PostTripComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance activity: Activity): PostTripComponent
    }

    fun inject(activity: PostTripActivity)
    fun inject(fragment: Step1Fragment)
    fun inject(fragment: Step2Fragment)
    fun inject(fragment: Step3Fragment)
}

@ActivityScope
@Subcomponent(modules = [LoginModule::class])
interface MainComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance activity: Activity): MainComponent
    }

    fun inject(fragment: HomeFragment)
    fun inject(activity: MainActivity)
    fun inject(fragment: TripDetailFragment)
    fun inject(fragment: UserProfileFragment)
}


