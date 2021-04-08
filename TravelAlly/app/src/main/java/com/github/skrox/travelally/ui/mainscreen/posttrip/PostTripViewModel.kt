package com.github.skrox.travelally.ui.mainscreen.posttrip

import android.util.Log
import android.view.View.OnFocusChangeListener
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.skrox.travelally.data.network.postobjects.PostTrip
import com.github.skrox.travelally.data.repositories.TripsRepository
import com.github.skrox.travelally.ui.mainscreen.posttrip.temp.PostTripFormFields
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat


class PostTripViewModel(private val tripsRepository: TripsRepository) : ViewModel() {

    var postTripListener: PostTripListener? = null
    var description: String = ""
    private var extraPeople: MutableList<Int> = mutableListOf()
    var startLat: Double = 0.0
    var startLon: Double = 0.0
    var endLat: Double = 0.0
    var endLon: Double = 0.0
    var startName: String = "sd"
    var destName: String = "gsv"
    var liveStartDate: MutableLiveData<String> = MutableLiveData()
    var liveEndDate: MutableLiveData<String> = MutableLiveData()

    private var post: PostTripForm = PostTripForm()
    private var onFocusEmail: OnFocusChangeListener? = null
    private var onFocusPassword: OnFocusChangeListener? = null

    init {
        Log.e("posttripmodel", "created")
    }

    fun addPeople(id: Int) {
        extraPeople.add(id)
    }

    fun removePeople(id: Int) {
        extraPeople.remove(id)
    }

    fun postTrip() {

        viewModelScope.launch {
            try {

                postTripListener?.onStarted()
                var startTime: String? = liveStartDate.value
                var endTime: String? = liveEndDate.value
                val fromServer = SimpleDateFormat("EEE, d MMM yyyy hh:mm aaa")
                val myFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                var date = fromServer.parse(startTime)
                startTime = myFormat.format(date)
                date = fromServer.parse(endTime)
                endTime = myFormat.format(date)
                Log.e("datee", endTime)
                tripsRepository.postTrip(
                    PostTrip(
                        startTime,
                        endTime,
                        description,
                        extraPeople,
                        startLat,
                        startLon,
                        endLat,
                        endLon,
                        startName,
                        destName
                    )
                )
                Log.e("post trip listener", ""+postTripListener)
                postTripListener?.onSuccess()
            } catch (e: Exception) {
                postTripListener?.onFailure(e.message ?: "Unknown cause")
                e.printStackTrace()
            }

        }
    }


//    @VisibleForTesting
//    fun init() {
//
////        onFocusEmail = OnFocusChangeListener { view, focused ->
////            val et = view as EditText
////            if (et.text.length > 0 && !focused) {
////                post?.isEmailValid(true)
////            }
////        }
////        onFocusPassword = OnFocusChangeListener { view, focused ->
////            val et = view as EditText
////            if (et.text.length > 0 && !focused) {
////                post?.isPasswordValid(true)
////            }
////        }
//    }

    fun getEmailOnFocusChangeListener(): OnFocusChangeListener? {
        return onFocusEmail
    }

    fun getPasswordOnFocusChangeListener(): OnFocusChangeListener? {
        return onFocusPassword
    }

    fun onButtonClick() {
//        Log.e("ptvm", "clicked")
//        post.onClick()
        postTrip()
    }

    fun getPostFields(): MutableLiveData<PostTripFormFields>? {
        return post.postFields
    }

    fun getForm(): PostTripForm {
        return post
    }

    companion object {
        @JvmStatic
        @BindingAdapter("bindServerDate")
        fun bindServerDate(textView: TextView, date: String?) {
            /*Parse string data and set it in another format for your textView*/
            textView.text = date
        }
    }

//    companion object {
//        @JvmStatic
//        @BindingAdapter("error")
//        fun setError(editText: EditText, strOrResId: Any?) {
//            if (strOrResId is Int) {
//                editText.error = editText.context.getString((strOrResId as Int?)!!)
//            } else {
//                editText.error = strOrResId as String?
//            }
//        }
//    }


//    @BindingAdapter("onFocus")
//    fun bindFocusChange(
//        editText: EditText,
//        onFocusChangeListener: OnFocusChangeListener?
//    ) {
//        if (editText.onFocusChangeListener == null) {
//            editText.onFocusChangeListener = onFocusChangeListener
//        }
//    }
}
