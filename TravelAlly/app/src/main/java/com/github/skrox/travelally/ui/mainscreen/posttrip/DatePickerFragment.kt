package com.github.skrox.travelally.ui.mainscreen.posttrip

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.text.SimpleDateFormat
import java.util.*


class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private var myYear: Int = 0
    private var myday: Int = 0
    private var myMonth: Int = 0


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(requireContext(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // Do something with the date chosen by the user
        Log.e(
            "date",
            "Year: " + year + "\n" +
                    "Month: " + month + "\n" +
                    "Day: " + day + "\n"
        );

        val c = Calendar.getInstance()
        val hour = c[Calendar.HOUR]
        val minute = c[Calendar.MINUTE]
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            this,
            hour,
            minute,
//            DateFormat.is24HourFormat(requireContext())
            false
        )
        myYear = year
        myday = day
        myMonth = month
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        Log.e(
            "date",
            "Year: " + hourOfDay + "\n" +
                    "Month: " + minute + "\n"
        );

        val fromServer = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
        val myFormat = SimpleDateFormat("EEE, d MMM yyyy hh:mm aaa")
        myMonth++
        val inputDateStr: String =
            myYear.toString() + '-' + myMonth.toString() + '-' + myday.toString() + 'T' + hourOfDay.toString() + ':' + minute.toString()
        val date = fromServer.parse(inputDateStr)
        val outputDateStr = myFormat.format(date)
        Log.e("date", outputDateStr)
        targetFragment!!.onActivityResult(
            targetRequestCode,
            Activity.RESULT_OK,
            Intent().putExtra("selectedDate", outputDateStr)
        )
    }

}
