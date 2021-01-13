package com.github.skrox.travelally.ui.mainscreen.posttrip

import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import com.github.skrox.travelally.BR
import com.github.skrox.travelally.ui.mainscreen.posttrip.temp.PostTripFormErrors
import com.github.skrox.travelally.ui.mainscreen.posttrip.temp.PostTripFormFields

class PostTripForm : BaseObservable() {
    private val fields: PostTripFormFields = PostTripFormFields()
    private val errors: PostTripFormErrors = PostTripFormErrors()
    private val postLiveData: MutableLiveData<PostTripFormFields> = MutableLiveData()

    @get:Bindable
    val isValid: Boolean
        get() {
            val valid = true
//            var valid = isStartTimeValid(false)
//            valid = isEndTimeValid(false) && valid
            notifyPropertyChanged(BR.startTimeError)
            notifyPropertyChanged(BR.endTimeError)
            return valid
        }

//    fun isEmailValid(setMessage: Boolean): Boolean {
//        // Minimum a@b.c
//        val email: String = fields.getEmail()
//        if (email != null && email.length > 5) {
//            val indexOfAt = email.indexOf("@")
//            val indexOfDot = email.lastIndexOf(".")
//            return if (indexOfAt > 0 && indexOfDot > indexOfAt && indexOfDot < email.length - 1) {
//                errors.setEmail(null)
//                notifyPropertyChanged(BR.valid)
//                true
//            } else {
//                if (setMessage) {
//                    errors.setEmail(R.string.error_format_invalid)
//                    notifyPropertyChanged(BR.valid)
//                }
//                false
//            }
//        }
//        if (setMessage) {
//            errors.setEmail(R.string.error_too_short)
//            notifyPropertyChanged(BR.valid)
//        }
//        return false
//    }
//
//    fun isPasswordValid(setMessage: Boolean): Boolean {
//        val password: String = fields.getPassword()
//        return if (password != null && password.length > 5) {
//            errors.setPassword(null)
//            notifyPropertyChanged(BR.valid)
//            true
//        } else {
//            if (setMessage) {
//                errors.setPassword(R.string.error_too_short)
//                notifyPropertyChanged(BR.valid)
//            }
//            false
//        }
//    }

    fun onClick() {
        Log.e("ptf", "here")
        postLiveData.postValue(fields)

    }

    val postFields: MutableLiveData<PostTripFormFields>
        get() = postLiveData

    fun getFields(): PostTripFormFields {
        return fields
    }

    @get:Bindable
    val startTimeError: Int
        get() = errors.start_time

    @get:Bindable
    val endTimeError: Int
        get() = errors.end_time
}
