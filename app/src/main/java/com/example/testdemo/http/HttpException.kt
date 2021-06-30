package com.example.testdemo.http

import java.lang.Exception

open class BaseHttpException(var code:Int,var msg:String,var exception:Throwable?):Exception(msg,exception){


}

class LocalException(throwable: Throwable) :BaseHttpException(11111,throwable.message ?: "",throwable)

class ServerCodeException(code: Int, msg: String) : BaseHttpException(code, msg, null) {
    constructor(data:DataBean<*>):this(data.code,data.msg!!)
}

