package com.github.skrox.travelally.di

import android.app.Activity
import com.github.skrox.travelally.R
import com.github.skrox.travelally.data.network.MyApi
import com.github.skrox.travelally.data.network.NetworkConnectionInterceptor
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class UtilsModule{


    @Provides
    fun OkHttpClientProvider(networkConnectionInterceptor: NetworkConnectionInterceptor):OkHttpClient{
        val okkHttpclient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .build()
        return okkHttpclient
    }

    @Provides
    fun MyApiProvider(okkHttpclient:OkHttpClient):MyApi{
        return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl("http://192.168.1.110:8080/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApi::class.java)
    }
}

@Module
class LoginModule{

    @Provides
    fun GoogleSignInClientProvider(activity: Activity): GoogleSignInClient{
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(activity.resources.getString(R.string.google_client_id))
                .build()

        val mGoogleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(activity, gso);

        return mGoogleSignInClient
    }

}


// This module tells AppComponent which are its subcomponents
@Module(subcomponents = [LoginComponent::class, MainComponent::class])
class AppSubcomponents{}