package com.github.skrox.travelally.data.network
import android.util.Log
import com.github.skrox.travelally.util.ApiException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

abstract class SafeApiRequest {

    suspend fun<T: Any> apiRequest(call: suspend () -> Response<T>) : T {

        try {
            val response = call.invoke()
            Log.e("response", response.toString())
            if (response.isSuccessful) {
                return response.body()!!
            } else {
                val error = response.errorBody()?.string()

                val message = StringBuilder()
                error?.let {
                    try {
                        message.append(JSONObject(it).getString("msg"))
                    } catch (e: JSONException) {
                    }
                    message.append("\n")

                }
                message.append("Error Code: ${response.code()}")
                throw ApiException(message.toString())
            }
        }catch (e:Exception){
            e.printStackTrace()
            throw ApiException(e.message?:e.toString())
        }
    }

}