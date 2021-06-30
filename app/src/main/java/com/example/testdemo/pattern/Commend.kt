package com.example.testdemo.pattern

/**
 * 命令式
 */
class Commend {
}

class Stock{
    fun buy() {
        println("Buy")
    }

    fun sell() {
        println("sell")
    }
}

interface Order{
    fun execute()
}

class BuyStock(var stock: Stock):Order{
    override fun execute() {
        stock.buy()
    }

}

class SellStock(var stock: Stock):Order{
    override fun execute() {
        stock.sell()
    }

}

class Manager(){

    var list:MutableList<Order> = ArrayList()
    fun takerOrder(order: Order) {
        list.add(order);
    }

    fun placeOrder() {
        for (l in list) {
            l.execute()
        }
        list.clear()
    }
}

fun main() {
    val manager = Manager()
    val stock = Stock()
    val buyStock = BuyStock(stock)
    val sellStock = SellStock(stock)

    manager.takerOrder(buyStock)
    manager.placeOrder()

    manager.takerOrder(sellStock)
    manager.placeOrder()

}