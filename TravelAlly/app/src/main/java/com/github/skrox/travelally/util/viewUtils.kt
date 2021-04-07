package com.github.skrox.travelally.util

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

fun ProgressBar.show() {
    visibility = View.VISIBLE
}

fun ProgressBar.hide() {
    visibility = View.GONE
}

fun View.snackbar(message: String) {
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
fun ImageView.loadImage(url: String?) {
    if (!url.isNullOrEmpty()) {
        Picasso.get().load(url)
            .into(this)
        Log.e("utilimage", url)
    } else {
        Picasso.get().cancelRequest(this)
    }
}

@BindingAdapter(value = ["bind:startDate", "bind:endDate"])
fun TextView.dateText(startDate: String, endDate: String) {
    // Create a parser and formatter with its pattern respectively
    val parser =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()) // the initial pattern
    val formatter =
        SimpleDateFormat("d MMM", Locale.getDefault()) // the desired output pattern
    val formatter2 =
        SimpleDateFormat("d", Locale.getDefault()) // the desired output pattern


    // Put the date in the parser, convert the string to Date class
    val parseStart = parser.parse(startDate)
    val parseEnd = parser.parse(endDate)
    val cal = Calendar.getInstance()
    cal.time = parseStart
    val startMonth = cal[Calendar.MONTH]
    cal.time = parseEnd
    val endMonth = cal[Calendar.MONTH]
    var result: String
    if (startMonth == endMonth) {
        result = formatter2.format(parseStart)
    } else {
        result = formatter.format(parseStart)
    }

    result += " - "
    result += formatter.format(parseEnd)
    this.text = result
    // Format the result with formatter, and put the result in var named "after"
//    val result = formatter.format(parse!!)
}


fun AppCompatActivity.findNavControllerFromFragmentContainer(id: Int): NavController {
    val fragment = supportFragmentManager.findFragmentById(id)
    check(fragment is NavHostFragment) { ("Activity $this does not have a NavHostFragment") }
    return fragment.navController
}