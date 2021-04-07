package com.github.skrox.travelally.data.network

import com.github.skrox.travelally.data.db.entities.Trip
import com.github.skrox.travelally.data.db.entities.User
import com.github.skrox.travelally.data.db.entities.Vote
import com.github.skrox.travelally.data.network.postobjects.PostTrip
import com.github.skrox.travelally.data.network.postobjects.SendToken
import com.github.skrox.travelally.data.network.responses.AuthResponse
import com.github.skrox.travelally.data.network.responses.TripsResponse
import com.github.skrox.travelally.data.network.responses.UserSuggestionResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*
import javax.inject.Singleton

@Singleton
interface MyApi {

    @POST("user/login/")
    suspend fun userLogin(
        @Body id_token: SendToken?
    ): Response<AuthResponse>


    //    @Headers("Authorization: Token ${}")
    @GET("trips/popular/")
    suspend fun getPopularTrips(@HeaderMap headers: Map<String, String?>): Response<TripsResponse>

    @GET("trips/nearme/")
    suspend fun getTripsNearMe(
        @HeaderMap headers: Map<String, String?>,
        @QueryMap query: Map<String, Double>
    ): Response<TripsResponse>

    @GET("trips/trips/")
    suspend fun getAllTrips(@HeaderMap headers: Map<String, String?>): Response<List<Trip>>

    @GET("trips/trips/{id}/")
    suspend fun getTrip(
        @Path("id") id: Int,
        @HeaderMap headers: Map<String, String?>
    ): Response<Trip>

    @POST("trips/request/{id}/")
    suspend fun requestToJoin(
        @Path("id") id: Int,
        @HeaderMap headers: Map<String, String?>
    ): Response<Unit>

//    @Headers("Accept: application/json")
    @POST("trips/trips/")
    suspend fun postTrip(
        @HeaderMap headers: Map<String, String?>,
        @Body trip: PostTrip
    ): Response<Trip>

    @POST("trips/vote/")
    suspend fun voteTrip(
        @HeaderMap headers: Map<String, String?>,
        @Body vote: Vote
    ): Response<Trip>

    @POST("trips/trips/{id}/upload-image/")
    suspend fun postTripImage(
        @Path("id") id: Int,
        @HeaderMap headers: Map<String, String?>,
        @Part file: MultipartBody.Part?
    ): Response<Trip>

    @OPTIONS("user/user/7")
    suspend fun getUserSuggestions(
        @HeaderMap headers: Map<String, String?>,
        @QueryMap query: Map<String, String>
    ): Response<List<UserSuggestionResponse>>

    @GET("user/user/{id}")
    suspend fun getUser(
        @Path("id") id: Int,
        @HeaderMap headers: Map<String, String?>
    ): Response<User>


//    companion object{
//        operator fun invoke(
//            networkConnectionInterceptor: NetworkConnectionInterceptor
//        ) : MyApi{
//
//            val okkHttpclient = OkHttpClient.Builder()
//                .addInterceptor(networkConnectionInterceptor)
//                .build()
//
//            return Retrofit.Builder()
//                .client(okkHttpclient)
//                .baseUrl("http://192.168.1.110:8080/api/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//                .create(MyApi::class.java)
//        }
//    }

}

