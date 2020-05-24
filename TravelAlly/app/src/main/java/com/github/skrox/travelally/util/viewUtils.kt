package com.github.skrox.travelally.util

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

fun ProgressBar.show(){
    visibility = View.VISIBLE
}

fun ProgressBar.hide(){
    visibility = View.GONE
}

fun View.snackbar(message: String){
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
        snackbar.setAction("Ok") {
            snackbar.dismiss()
        }
    }.show()
}

@BindingAdapter("image")
fun ImageView.loadImage(url: String?){
    if(!url.isNullOrEmpty()){
       Picasso.get().load(url)
            .into(this)
        Log.e("utilimage",url)
    }else{
        Picasso.get().cancelRequest(this)
    }
}
