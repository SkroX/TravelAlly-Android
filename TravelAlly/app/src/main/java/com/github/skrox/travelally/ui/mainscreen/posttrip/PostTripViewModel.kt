package com.github.skrox.travelally.ui.mainscreen.posttrip

import android.util.Log
import android.view.View.OnFocusChangeListener
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.skrox.travelally.data.network.postobjects.PostTrip
import com.github.skrox.travelally.data.repositories.TripsRepository
import com.github.skrox.travelally.ui.mainscreen.posttrip.temp.PostTripFormFields
import kotlinx.coroutines.launch

class PostTripViewModel(private val tripsRepository: TripsRepository) : ViewModel() {

    var startTime: String? = null
    var endTime: String? = null
    var description: String? = null
    private var extraPeople: MutableList<Int> = mutableListOf()
    private var startLat: Double = 0.0
    private var startLon: Double = 0.0
    private var endLat: Double = 0.0
    private var endLon: Double = 0.0
    private lateinit var startName: String
    private lateinit var destName: String

    private var trip: PostTrip? = null

    private var post: PostTripForm = PostTripForm()
    private var onFocusEmail: OnFocusChangeListener? = null
    private var onFocusPassword: OnFocusChangeListener? = null

    fun addPeople(id: Int) {
        extraPeople.add(id)
    }

    fun removePeople(id: Int) {
        extraPeople.remove(id)
    }

    fun postTrip() = viewModelScope.launch {
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
        Log.e("ptvm", "clicked")
        post.onClick()
    }

    fun getPostFields(): MutableLiveData<PostTripFormFields>? {
        return post.postFields
    }

    fun getForm(): PostTripForm {
        return post
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
