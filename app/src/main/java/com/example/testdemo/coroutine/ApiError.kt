package com.example.testdemo.coroutine

import java.io.IOException

object ApiError {
    val dataIsNull = Error(-1, "Data is null")
    val httpStatesCodeError = Error(-2, "Server error, Please try again later")
    val unknownError = Error(-3, "Unknown Error")
}

data class Error(val code: Int, val msg: String)

class ApiException(val code:Int,val msg: String):IOException()
