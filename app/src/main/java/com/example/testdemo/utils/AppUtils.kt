package com.example.testdemo.utils

import org.json.JSONObject

class AppUtils {
 companion object{
     fun isJson(json: String): Boolean {
         try {
             val jsonObject = JSONObject(json)
         } catch (e: Exception) {
             return false
         }
         return true
     }
 }
}