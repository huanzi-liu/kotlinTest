package com.example.testdemo.pattern

/**
 * 访问者式
 */
interface Visitor {
    fun visitor(cameraSystem: CameraSystem)
    fun visitor(imageSystem: ImageSystem)
}

abstract class Action{
    abstract fun accept(visitor: Visitor)
}

class CameraSystem:Action(){
    override fun accept(visitor: Visitor) {
        visitor.visitor(this)
    }

}

class ImageSystem : Action() {
    override fun accept(visitor: Visitor) {
        visitor.visitor(this)
    }

    fun getSize():Int = 10

}

class App :Visitor{
    override fun visitor(cameraSystem: CameraSystem) {
        println("访问相机")
    }

    override fun visitor(imageSystem: ImageSystem) {
        println("访问相册，一共:${imageSystem.getSize()}")
    }

}

fun main() {
    val app = App()
    val imageSystem = ImageSystem()
    imageSystem.accept(app)
    val cameraSystem = CameraSystem()
    cameraSystem.accept(app)
}

