package com.example.testdemo.singleclick

import android.app.Activity
import android.content.ContextWrapper
import android.os.SystemClock
import android.view.View
import com.example.testdemo.R
import java.lang.IllegalArgumentException


fun View.onSingleClick(interval: Int? = SingleClickUtil.singleClickInterval, isShareSingleClick: Boolean? = true, listener: View.OnClickListener? = null) {
    if (listener == null) {
        return
    }
    setOnClickListener {
        determineTriggerSingleClick(interval
                ?: SingleClickUtil.singleClickInterval, isShareSingleClick ?: true, listener)
    }
}

fun View.determineTriggerSingleClick(interval: Int = SingleClickUtil.singleClickInterval, isShareSingleClick: Boolean = true, listener: View.OnClickListener) {
    val target = if (isShareSingleClick) {
        getActivity(this)?.window?.decorView ?: this
    } else {
        null
    }
    val millis = target?.getTag(R.id.test) as? Long ?: 0
    if (SystemClock.uptimeMillis() - millis >= interval) {
        target?.setTag(R.id.test, SystemClock.uptimeMillis())
        listener.onClick(this)
    }

}


fun getActivity(view: View): Activity? {

    var context = view.context
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }
    return null
}

object SingleClickUtil {
    var singleClickInterval: Int = 1000
        set(value) {
            if (value <= 0) {
                throw IllegalArgumentException("Single click interval must be greater than 0.")
            }
            field = value
        }
}