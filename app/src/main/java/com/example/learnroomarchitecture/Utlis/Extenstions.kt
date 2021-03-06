package com.example.learnroomarchitecture.Utlis

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun Date.toSimpleDateFomat(): String{
    val fomat = SimpleDateFormat("YYYY-MM-dd hh:mm")
    return fomat.format(this)
}