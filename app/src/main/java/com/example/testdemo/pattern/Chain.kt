package com.example.testdemo.pattern

/**
 * 责任链式
 */
class Chain {
}

interface Interceptor{
    fun chain(data:String):String
}
class CacheInterceptor:Interceptor{
    override fun chain(data: String): String = "$data 加缓存"
}

class CallServerInterceptor:Interceptor{
    override fun chain(data: String): String = "$data 呼叫了服务"
}

class RealInterceptor() {
//    val cache= CacheInterceptor()
//    val call = CallServerInterceptor()
    var list:MutableList<Interceptor> = ArrayList()

    init {
        list.add(CacheInterceptor())
        list.add(CallServerInterceptor())
    }

    fun request(s:String):String{
        var res=""
        for (i in 0 until list.size) {
            res+= list[i].chain(s)
        }
        return res
    }
}

fun main() {
    val real = RealInterceptor()
    println(real.request("qq"))
}