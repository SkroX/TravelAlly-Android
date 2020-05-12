package com.github.skrox.travelally.ui.auth

interface AuthListener {

    fun onStarted()
    fun onSuccess()
    fun onFailure(msg:String)
}