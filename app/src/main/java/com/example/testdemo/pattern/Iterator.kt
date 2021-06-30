package com.example.testdemo.pattern

/**
 * 迭代器式
 */
interface Iterator {
    fun hasNext():Boolean
    fun next():Any?
}

interface Container{
    fun getIterator():Iterator
}

class NameContainer:Container{

    val names = arrayOf("name1","name2","name3","name4","name5")
    override fun getIterator(): Iterator  = NameIterator()

    inner class NameIterator:Iterator{
        var index = 0
        override fun hasNext(): Boolean {
            return index < names.size
        }

        override fun next(): Any? {
            return if (hasNext()){
                names[index++]
            }else {
                null
            }
        }

    }

}

fun main() {
    val nameContainer = NameContainer()
    val iterator = nameContainer.getIterator()
    while (iterator.hasNext()) {
        println("${iterator.next()}")
    }
}