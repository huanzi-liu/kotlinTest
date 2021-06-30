package com.example.testdemo.pattern

/**
 * 備忘錄式
 */
class Memento {
}

class Layer(var state:String)

class Manager1(){
    var layer:Layer? =null
    fun save(state: String) :Layer = Layer(state)

    fun restore(layer: Layer){
        this.layer = layer
    }

}

class PhotoShop(){
    var list:MutableList<Layer> = ArrayList()
    fun ctrlS(layer: Layer) {
        list.add(layer)
    }

    fun ctrlZ(index:Int):Layer = list[index]
}

fun main() {
    val photoShop = PhotoShop()
    val manager = Manager1()

    photoShop.ctrlS(manager.save("-"))
    photoShop.ctrlS(manager.save("="))
    manager.restore(photoShop.ctrlZ(0))
    println("當前：${manager.layer?.state}")
    photoShop.ctrlS(manager.save("*"))
    photoShop.ctrlS(manager.save("#"))
    manager.restore(photoShop.ctrlZ(1))
    println("當前：${manager.layer?.state}")

}