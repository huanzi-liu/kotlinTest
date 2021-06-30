package com.example.testdemo.reflex

import android.os.Build
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi

@ExperimentalUnsignedTypes
open class TestData(@Nullable private var name: String, private var number: UInt) {

    override fun toString(): String {
        return "TestData(name='$name', number=$number)"
    }
}

@ExperimentalUnsignedTypes
data class TestData2(private var code: Int = 111, var height: String = "188", var ss: String? = null) : TestData("e", 1.toUInt()), TestInterface {
    override fun login(code: Int, height: String): Boolean {
        return 333 == code && "177" == height
    }

    override fun toString(): String {
        return "TestData2(code=$code, height='$height')"
    }
}

interface TestInterface {
    fun login(code: Int, height: String): Boolean
}


@RequiresApi(Build.VERSION_CODES.O)
fun main(args: Array<String>) {
    val data = TestData2()
    val dataClass = TestData2::class.java
    val dataClass2 = data.javaClass

    println(dataClass.name)
    println(dataClass.`package`?.name)
    println(dataClass.simpleName)
    println(dataClass.superclass?.simpleName)
    for (s in dataClass.interfaces) {
        println(s.name)
    }
    val d = dataClass.getDeclaredConstructor().newInstance()
    d.height = "177"
    println(d.height)

    println(dataClass.getDeclaredField("code"))
//    println(dataClass.getField("ss"))
    for (d in dataClass.declaredClasses) {
        println(d.name)
    }
    for (d in dataClass2.fields) {
        println("sss${d.name}")
    }
    println(dataClass.getMethod("login",Int::class.java,String::class.java))
    println(dataClass.getDeclaredMethod("login",Int::class.java,String::class.java))
//    for (m in dataClass2.declaredMethods) {
//        println("sss ${m.name}")
//    }
//    for (m in dataClass.methods) {
//        println(m.name)
//    }
    println(dataClass.getMethod("login",Int::class.java,String::class.java).invoke(TestData2(),333,"177"))

//    for (c in dataClass.declaredConstructors){
//        println("ccc ${c.name}")
//        for (p in c.parameters) {
//            println("ppp ${p.name}")
//        }
//    }
//    for (c in dataClass.constructors) {
//        println("ccc2 ${c.name}")
//        for (p in c.parameters) {
//            println("ppp2 ${p.type}")
//        }
//
//    }
    println(dataClass.declaredConstructors[0].newInstance(333,"177","ss"))
}

