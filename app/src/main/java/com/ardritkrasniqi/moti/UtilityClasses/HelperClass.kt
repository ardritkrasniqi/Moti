package com.ardritkrasniqi.moti.UtilityClasses

import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object HelperClass {

    // function to hide soft keyboard
    fun hideSoftKeyBoard(context: Context, view: View) {
        try {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // function to format date as string format
    fun dateFormatterForForecastRecycler(dateAsString: String): String{
        val to: DateFormat = SimpleDateFormat("dd MMM", Locale.getDefault()) // wanted format
        val from: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // current format
        return to.format(from.parse(dateAsString)!!)
    }

    fun getDayForForecastRecycler(dateAsString: String): String{
        val to: DateFormat = SimpleDateFormat("EEEEE", Locale.getDefault()) // wanted format
        val from: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // current format
        return to.format(from.parse(dateAsString)!!)
    }

    fun addFiveDaysForRecycler(startDate: String, numOfDaysToAdd: Int): MutableList<String>{
        val lista: MutableList<String> = mutableListOf()
        for (i in 0..numOfDaysToAdd){
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        calendar.time = sdf.parse(startDate)!!
        calendar.add(Calendar.DAY_OF_MONTH, i)
            lista.add(sdf.format(calendar.time))

        }
        return lista
    }
}
