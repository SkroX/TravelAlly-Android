package com.github.skrox.travelally.util

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import java.util.*

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
        val snackbarView: View = snackbar.getView()
        val textView =
            snackbarView.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
        textView.maxLines = 5
    }.show()

}

@BindingAdapter("image")
fun ImageView.loadImage(url: String?){
    if(!url.isNullOrEmpty()){
       Picasso.get().load(url).resize(0, 120).centerCrop()
            .into(this)
        Log.e("utilimage", url)
    }else{
        Picasso.get().cancelRequest(this)
    }
}
