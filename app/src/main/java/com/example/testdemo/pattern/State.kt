package com.example.testdemo.pattern

import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

/**
 * 状态式
 */

class MediaPlayer() {
    var state: State1? = null
}

interface State1 {
    fun doAction(player: MediaPlayer)
}

class StartState : State1 {
    override fun doAction(player: MediaPlayer) {
        println("$player start")
    }
}

class StopState : State1 {
    override fun doAction(player: MediaPlayer) {
        println("$player stop")
    }
}


interface State {
    fun insert()
    fun back()
    fun turnCrank()
    fun dispense()
}

class NoMoney(var vendingMachine: VendingMachine) : State {
    override fun insert() {
        println("tou")
        vendingMachine.state = vendingMachine.hasMoney
    }

    override fun back() {
        println("tui?")
    }

    override fun turnCrank() {
        println("na?")
    }

    override fun dispense() {
        throw IllegalStateException("fei!")
    }
}

class HasMoney(var vendingMachine: VendingMachine) : State {
    override fun insert() {
        println("tou!")
    }

    override fun back() {
        println("tui")
        vendingMachine.state = vendingMachine.noMoney
    }

    override fun turnCrank() {
        println("zhuan")
        vendingMachine.state = vendingMachine.sold
    }

    override fun dispense() {
        throw IllegalStateException("fei!")
    }

}

class Sold(var vendingMachine: VendingMachine) : State {
    override fun insert() {
        println("chu,wu")
    }

    override fun back() {
        println("chu,mei")
    }

    override fun turnCrank() {
        println("chu,chong")
    }

    override fun dispense() {
        vendingMachine.dispense()
        if (vendingMachine.count > 0) {
            vendingMachine.noMoney
        } else {
            println("yao")
            vendingMachine.soldOut
        }
    }
}


class SoldOut(var vendingMachine: VendingMachine) : State {
    override fun insert() {
        println("shiBai,yao")
    }

    override fun back() {
        println("wei,tui?")
    }

    override fun turnCrank() {
        println("yao,wu")
    }

    override fun dispense() {
        throw IllegalStateException("fei")
    }
}

class VendingMachine(var count: Int) {
    var noMoney: State = NoMoney(this)
    var hasMoney: State = HasMoney(this)
    var sold: State = Sold(this)
    var soldOut: State = SoldOut(this)
    var state: State = noMoney

    fun insert() {
        state.insert()
    }

    fun back() {
        state.back()
    }

    fun turnCrank() {
        state.turnCrank()
    }

    fun dispense() {
        println("fa 1")
        if (count != 0) {
            count -= 1
        }
    }
}


fun main() {
    val player = MediaPlayer()
    val startState = StartState()
    startState.doAction(player)
    val stopState = StopState()
    stopState.doAction(player)


    val vendingMachine = VendingMachine(10)
    vendingMachine.insert()
    vendingMachine.back()

    vendingMachine.insert()
    vendingMachine.turnCrank()
//    vendingMachine.dispense()
    vendingMachine.back()
    vendingMachine.back()

    vendingMachine.insert()
    vendingMachine.turnCrank()

}



