package com.example.testdemo.utils

import com.google.gson.Gson

class GsonUtil {

    companion object {
        var gson = Gson()

        fun getBeanToJson(bean:Any?) :String{
            if (bean == null) {
                return ""
            }
            return gson.toJson(bean)
        }
    }
}