package com.ardritkrasniqi.moti.UtilityClasses

import android.content.Context
import android.util.Log
import android.view.View
import android.view.animation.BounceInterpolator
import android.view.animation.OvershootInterpolator
import android.view.animation.ScaleAnimation
import android.view.inputmethod.InputMethodManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
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

    // gets the day from dateText format and returns only the first font of it
    fun getDayForForecastRecycler(dateAsString: String): String{
        val to: DateFormat = SimpleDateFormat("EEEEE", Locale.getDefault()) // wanted format
        val from: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // current format
        return to.format(from.parse(dateAsString)!!)
    }

    // gets the day from dateText format and returns full day
    fun getDayForForecastRecyclerFull(dateAsString: String): String{
        val to: DateFormat = SimpleDateFormat("EEEE", Locale.getDefault()) // wanted format
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



    // FAB SHOW ANIMATION :D
    fun fabAnimationShow(fab: FloatingActionButton){
        val anim = ScaleAnimation(0F, 1F, 0F, 1F)
        anim.fillBefore = true
        anim.fillAfter = true
        anim.isFillEnabled = true
        anim.duration = 300
        anim.interpolator = OvershootInterpolator()
        fab.startAnimation(anim)
    }

    // FAB HIDE ANIMATION :D
    fun fabAnimationHide(fab: FloatingActionButton){
        val anim = ScaleAnimation(1F, 0F, 1F, 0F)
        anim.fillBefore = false
        anim.fillAfter = true
        anim.isFillEnabled = true
        anim.duration = 300
        anim.interpolator = OvershootInterpolator()
        fab.startAnimation(anim)
    }
}
