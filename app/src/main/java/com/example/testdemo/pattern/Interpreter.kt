package com.example.testdemo.pattern

/**
 * 解释器式
 */
interface Interpreter {
    fun interpreter(sql:String):String
}

class SelectInterpreter:Interpreter{
    override fun interpreter(sql: String): String {
        if (sql.indexOf("查") == 0) {
            val start= sql.indexOf("查")+1
            val end = sql.length
            println("查询：${sql.substring(start,end)}")
        }else{
            println("error")
        }
        return ""
    }

}

fun main() {
    val select = SelectInterpreter()
    select.interpreter("查1234")
}