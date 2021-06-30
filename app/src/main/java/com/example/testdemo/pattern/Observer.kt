package com.example.testdemo.pattern

import java.util.*
import kotlin.collections.ArrayList

class Teacher {
    var list: MutableList<Student> = ArrayList()
    var i: Int = 0
    fun attach(student: Student) {
        list.add(student)
    }

    fun dispatchHomeWork(i: Int) {
        this.i = i
        for (l in list) {
            l.doHomeWork(i)
        }
    }
}

class Student(teacher: Teacher) : DoWork() {
    init {
        this.teacher =teacher
        teacher.attach(this)
    }

    override fun doHomeWork(i: Int) {
        println("发布 $i")
    }

}

abstract class DoWork {
    var teacher: Teacher? = null
    abstract fun doHomeWork(i: Int)
}






interface Observer {
    fun update(subject: Subject)
}

abstract class Subject() {
    var list: MutableList<Observer> = ArrayList()
    fun notifyObserver() {
        for (l in list) {
            l.update(this)
        }
    }
}

class ConcreteSubject : Subject() {
    var state: Int = 0
        set(value) {
            field = value
            notifyObserver()
        }

}

class ConcreteObserver : Observer {
    var state: Int = 0
    override fun update(subject: Subject) {
        val concreteSubject = subject as ConcreteSubject
        state = concreteSubject.state
    }

}


class Subject3D:Observable(){
    var msg:String =""
    set(value) {
        field = value
        setChanged()
        notifyObservers()
    }
}

class SubjectSSQ : Observable() {
    var msg:String= ""
    set(value) {
        field = value
        setChanged()
        notifyObservers()
    }
}

class ObserverO:java.util.Observer{

    fun registerObserver(obs:Observable) {
        obs.addObserver(this)
    }

    override fun update(o: Observable?, arg: Any?) {
        if (o is Subject3D){
            val obs:Subject3D = o
        println("${obs.msg} 3d")
        }
        if (o is SubjectSSQ) {
            val obs:SubjectSSQ = o
            println("${obs.msg} SSQ")
        }
    }

}

fun main() {
    val teacher = Teacher()
    Student(teacher)
    Student(teacher)
    Student(teacher)

    teacher.dispatchHomeWork(1)
    teacher.dispatchHomeWork(3)


    val concreteSubject = ConcreteSubject()
    val concreteObserver1 = ConcreteObserver()
    val concreteObserver2 = ConcreteObserver()
    val concreteObserver3 = ConcreteObserver()

    concreteSubject.list.add(concreteObserver1)
    concreteSubject.list.add(concreteObserver2)
    concreteSubject.list.add(concreteObserver3)

    concreteSubject.state = 10
    println("obs1 ${concreteObserver1.state}")
    println("obs2 ${concreteObserver2.state}")
    println("obs3 ${concreteObserver3.state}")

    val s3d = Subject3D()
    val ssq = SubjectSSQ()
    val obs = ObserverO()

    obs.registerObserver(s3d)
    obs.registerObserver(ssq)
    s3d.msg = "3d"
    ssq.msg = "ssq"

}
