package com.github.skrox.travelally.di

import android.app.Activity
import android.content.Context
import com.github.skrox.travelally.R
import com.github.skrox.travelally.data.network.MyApi
import com.github.skrox.travelally.data.network.NetworkConnectionInterceptor
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class UtilsModule {


    @Provides
    fun OkHttpClientProvider(networkConnectionInterceptor: NetworkConnectionInterceptor): OkHttpClient {
        val logging = HttpLoggingInterceptor()
// set your desired log level
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val okkHttpclient = OkHttpClient.Builder()
            .addInterceptor(networkConnectionInterceptor)
            .addInterceptor(logging)
            .build()
        return okkHttpclient
    }

    @Provides
    fun MyApiProvider(okkHttpclient: OkHttpClient): MyApi {
        return Retrofit.Builder()
            .client(okkHttpclient)
            .baseUrl("http://3.16.156.100:8080/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MyApi::class.java)
    }
}

@Module
class LoginModule {

    @Provides
    fun GoogleSignInClientProvider(activity: Activity): GoogleSignInClient {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(activity.resources.getString(R.string.google_client_id))
                .build()

        val mGoogleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(activity, gso);

        return mGoogleSignInClient
    }

}

@Module
class PlacesAPiModule {

    @Singleton
    @Provides
    fun PlacesClientProvider(context: Context): PlacesClient {
        // Initialize the SDK
        Places.initialize(
            context.applicationContext,
            context.getString(R.string.google_place_api_key)
        );

        // Create a new Places client instance
        val placesClient: PlacesClient = Places.createClient(context);
        return placesClient
    }


}


// This module tells AppComponent which are its subcomponents
@Module(subcomponents = [LoginComponent::class, MainComponent::class])
class AppSubcomponents {}