package com.ardritkrasniqi.moti

import android.widget.TextView

object Converter{
    @JvmStatic
    fun doubleToInt(view: TextView,  oldValue: Double): Int{
        return oldValue.toInt()
    }
}