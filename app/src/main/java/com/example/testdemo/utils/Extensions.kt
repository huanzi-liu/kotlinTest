package com.example.testdemo.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.testdemo.base.BaseApplication

fun Int.asColor() = ContextCompat.getColor(BaseApplication.application, this)

fun Int.asDrawable() = ContextCompat.getDrawable(BaseApplication.application, this)

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.INVISIBLE
}

fun View.remove() {
    this.visibility = View.GONE
}

fun Context.showShortToast(message:String){
    Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
}

fun Context.showLongToast(message:String){
    Toast.makeText(this,message,Toast.LENGTH_LONG).show()
}

fun String?.valid(): Boolean {
    return this != null && !this.equals("null", true) && this.trim().isNotEmpty()
}

inline fun <reified T : Any> Activity.getValue(label: String, defaultValue: T? = null): Any? {
    val value = intent?.extras?.get(label)
    return if (value is T) value else defaultValue
}

inline fun <reified T : Any> Activity.getValueNoNull(label: String, defaultValue: T? = null): Any {
    val value = intent?.extras?.get(label)
    return requireNotNull(if (value is T) value else defaultValue) { label }
}