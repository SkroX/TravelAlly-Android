package com.github.skrox.travelally.ui.mainscreen.posttrip

interface PostTripListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure(msg: String)
}