package com.example.testdemo.algorithm

import java.util.*
import java.util.function.IntBinaryOperator
import kotlin.collections.ArrayList

class Solution {
    /**
     * +*-/()运算
     */
    fun calculate(s: String): Int {
        return s.toRPN().evalRPN()
    }

    fun String.toRPN(): List<String> {
        var list = ArrayList<String>()
        var stack = ArrayDeque<Char>()
        var s = CharArray(6)

        var num = 0
        var sign = 1

        for ((index, token) in this.withIndex()) {
            when (token) {
                ' ' -> continue
                in '0'..'9' -> {
                    num = num * 10 + (token.toInt() - 48)
                    if (index + 1 >= length || (this[index + 1] !in '0'..'9')) {
                        list.add("${sign * num}")
                        num = 0
                    }
                }
                '(' -> stack.push(token)
                ')' -> {
                    while ('(' != stack.peek()) {
                        list.add("${stack.pop()}")
                    }
                    stack.pop()
                }
                else -> {
                    while (token.priority() <= stack.peek().priority()) {
                        list.add("${stack.pop()}")
                    }
                    stack.push(token)
                }
            }
        }
        while (stack.isNotEmpty()) {
            list.add("${stack.pop()}")
        }

        return list
    }

    fun Char?.priority(): Int {
        return when (this) {
            '+', '-' -> 1
            '*', '/' -> 2
            null -> -1
            else -> -1
        }
    }

    fun Deque<Int>.eval(operator: (Int, Int) -> Int): Int {
        if (size > 1) {
            val num1 = pop()
            val num2 = pop()
            return operator(num2, num1)
        } else {
            return operator(0, pop())
        }
    }

    fun List<String>.evalRPN(): Int {
        var stack = ArrayDeque<Int>()
        for (token in this) {
            when (token) {
                "+" -> {
                    stack.push(stack.eval { o1, o2 -> o1 + o2 })
                }
                "-" -> {
                    stack.push(stack.eval { o1, o2 -> o1 - o2 })
                }
                "*" -> {
                    stack.push(stack.eval { o1, o2 -> o1 * o2 })
                }
                "/" -> {
                    stack.push(stack.eval { o1, o2 -> o1 / o2 })
                }
                else -> {
                    stack.push(token.toInt())
                }
            }
        }
        return stack.pop()
    }

    fun searchLeft(numbers: IntArray, target: Int): Int {
        if (numbers.isEmpty()) {
            return -1
        }
        var left = 0
        var right = numbers.size - 1
        while (left < right) {
            val mid = (left + right) ushr 1
//            val mid = left + (right - left) /2
            if (numbers[mid] < target) {
                left = mid + 1
            } else {
                right = mid
            }
        }
        return if (numbers[left] == target) left else -1
    }

    fun searchRight(numbers: IntArray, target: Int): Int {
        if (numbers.isEmpty()) {
            return -1
        }
        var left = 0
        var right = numbers.size - 1
        while (left < right) {
            val mid = (left + right  + 1) ushr 1
//            val mid = left + (right - left + 1) /2
            if (numbers[mid] > target) {
                right = mid - 1
            } else {
                left = mid
            }
        }
        return if (numbers[left] == target) left else -1
    }
}