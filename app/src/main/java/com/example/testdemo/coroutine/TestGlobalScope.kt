package com.example.testdemo.coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.IOException
import java.lang.AssertionError
import java.lang.RuntimeException
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.CoroutineContext
import kotlin.system.measureTimeMillis

class TestGlobalScope {

    companion object {

        fun test() = runBlocking() {
//            GlobalScope.launch {
//                delay(1000)
//                println("tttt")
//            }
//            println("ssss")
//            delay(2000)

//            val job = GlobalScope.launch {
//                delay(1000)
//                println("111111")
//            }
//            println("22222")
//            job.join()

            launch {
                delay(1000)
                println("tttt")
            }
            println("ssss")
        }

        fun test2() = runBlocking {
            launch {
                delay(100)
                println("11")
            }

            coroutineScope {
                launch {
                    delay(100)
                    println("22")
                }
                delay(100)
                println("33")
            }
            println("44")
        }

        fun test3() = runBlocking {
//            repeat(10000) {
//                launch {
//                    delay(1000)
//                    print(".")
//                }
//            }

            GlobalScope.launch {
                repeat(1000) {
                    delay(1000)
                    println("---")
                }
            }
            delay(2500)

        }

        fun test4() = runBlocking {
//            val job = launch {
//                repeat(1000) {
//                    delay(1000)
//                    println("$it")
//                }
//            }
//            delay(2300)
//            println("s")
//            job.cancel()
//            job.join()
//            println("e")

//            val time = System.currentTimeMillis()
//            val job = launch(Dispatchers.Default) {
//                var times = time
//                var i = 0
////                while (i < 5) {
//                while (isActive) {
//                    if (System.currentTimeMillis() >= times) {
//                        println("sss ${i++}")
//                        times += 500
//                    }
//                }
//            }
//            delay(1300)
//            println("s")
//            job.cancelAndJoin()
//            println("e")

//            val job = launch {
//
//                try {
//                    repeat(1000) {
//                        delay(1000)
//                        println("$it")
//                    }
//                } finally {
//                    println("ff")
//                }
//            }
//            delay(2300)
//            println("s")
//            job.cancelAndJoin()
//            println("e")

            val job = launch {

                try {
                    repeat(1000) {
                        delay(1000)
                        println("$it")
                    }
                } finally {
                    withContext(NonCancellable) {
                        println("ff")
                        delay(1000)
                        println("ss")

                    }

                }
            }
            delay(2300)
            println("s")
            job.cancelAndJoin()
            println("e")


        }

        fun test5() = runBlocking {
            val result = withTimeoutOrNull(6100) {
                repeat(10) {
                    delay(100)
                    println("dd")
                }
                "s"
            }
            println("$result")

        }

        fun test6() {
//            runBlocking {
//                repeat(1000) {
//                    launch {
//                        val res = withTimeout(60){
//                            delay(50)
//                            Res()
//                        }
//                        res.close()
//                    }
//                }
//            }
//            println("$a")

            runBlocking {
                repeat(1000) {
                    launch {
                        var res: Res? = null
                        try {
                            withTimeout(60) {
                                delay(50)
                                res = Res()
                            }
                        } finally {
                            res?.close()
                        }
                    }
                }
            }
            println("$a")
        }

        suspend fun one(): Int {
            delay(1000)
            return 10
        }

        suspend fun two(): Int {
            delay(500)
            return 8
        }

        suspend fun tAsync(): Int = coroutineScope {
            val one = async { one() }
            val two = async { two() }
            one.await() + two.await()
        }

        fun test7() = runBlocking {
//            val time = measureTimeMillis {
//                println("${one()+ two()}")
//            }
//            println("$time s")

//            val time = measureTimeMillis {
//                val one = async{one()}
//                val two = async{two()}
//                println("${one.await()+ two.await()}")
//            }
//            println("$time s")

            val time = measureTimeMillis {
                println("${tAsync()}")
            }
            println("$time s")

        }

        fun test8() = runBlocking {
//            launch {
//                println("1 ${Thread.currentThread().name}")
//            }
//            launch(Dispatchers.Unconfined) {
//                println("2 ${Thread.currentThread().name}")
//            }
//            launch(Dispatchers.Default) {
//                println("3 ${Thread.currentThread().name}")
//            }
//            launch(newSingleThreadContext("--")) {
//                println("4 ${Thread.currentThread().name}")
//            }

            launch(Dispatchers.Unconfined) {
                println("1 ${Thread.currentThread().name}")
                delay(1000)
                println("2 ${Thread.currentThread().name}")
            }

            launch {
                println("3 ${Thread.currentThread().name}")
                delay(1000)
                println("4 ${Thread.currentThread().name}")

            }
            println("${kotlin.coroutines.coroutineContext[Job]} ")
        }

        fun log(msg: String) {
            println("${Thread.currentThread().name} $msg")
        }

        fun test9() {
            newSingleThreadContext("t1").use { t1 ->
                newSingleThreadContext("t2").use {
                    runBlocking(t1) {
                        log("t1")
                        withContext(it) {
                            log("t2")
                        }
                        log("t3")
                    }

                }
            }

        }

        fun test10() = runBlocking {
//            val job = launch {
//                GlobalScope.launch {
//                    println("1")
//                    delay(1000)
//                    println("2")
//                }
//                launch {
//                    println("3")
//                    delay(1000)
//                    println("4")
//                }
//            }
//            delay(1000)
//            job.cancel()
//            delay(1000)
//            println("5")

            val job = launch {
                repeat(3) {
                    launch {
                        delay(1000)
                        println("$it s")
                    }
                }
                println("e")
            }
            job.join()
            println("end")
        }

        fun test11() = runBlocking(CoroutineName("m")) {
            val one = async(CoroutineName("t1")) {
                delay(1000)
                log("1 ")
                100
            }
            val two = async(CoroutineName("t2")) {
                delay(1000)
                log("2 ")
                5
            }
            log("${one.await() / two.await()}")

        }

        suspend fun s(): List<Int> {
            delay(1000)
            return listOf(1, 2, 3)
        }

        fun ss(): Flow<Int> = flow {
            for (i in 1..3) {
                delay(100)
                emit(i)
            }

        }

        fun test12() = runBlocking {
//            s().forEach { println(it) }

//            launch {
//                for (i in 1..3) {
//                    println("ttt $i")
//                delay(100)
//                }
//            }
//            ss().collect{ println("$it")}

//            val s = ss()
//            s.collect{ println("$it")}
//            s.collect{ println("$it")}

//            withTimeoutOrNull(230){
//                ss().collect{ println("$it")}
//            }

            (1..3).asFlow().collect { println("$it") }

        }

        suspend fun request(res: Int): String {
            delay(200)
            return "ddd $res"
        }

        fun test13() = runBlocking {
//            (1..3).asFlow().map { request(it) }.collect{ println(it)}

            (1..3).asFlow().transform {
                emit("ssss $it")
                emit(request(it))
            }.collect { println(it) }
        }

        fun number(): Flow<Int> = flow {
            try {
                emit(1)
                emit(2)
                emit(3)
                println("end")
                emit(4)
            } finally {
                println("break")
            }
        }

        fun test14() = runBlocking {
//            number().take(3).collect { println(it) }

//            val sum = (1..60).asFlow().map { it*it }.reduce { q, w -> q+w  }
//            println(sum)

//            (1..6).asFlow().filter {
//                println("sss $it")
//                it % 3 == 0
//            }.map {
//                println("mmm $it")
//                "tt $it"
//            }.collect { println(it) }

            (1..3).asFlow().scan(emptyList<Int>()) { a, b -> a + b }.toList().forEach { println(it) }
        }

        fun sss(): Flow<Int> = flow {
            for (i in 1..3) {
                Thread.sleep(100)
                log("e $i")
                emit(i)
            }

        }.flowOn(Dispatchers.Default)

        fun test15() = runBlocking {
//            sss().collect{ println(log(" $it"))}

//           val t = measureTimeMillis { ss().buffer().collect { delay(300)
//                println(it)}}
//            println("time $t s")

//            val t = measureTimeMillis { ss().conflate().collect { delay(300)
//                println(it)}}
//            println("time $t s")

//            val t = measureTimeMillis { ss().collectLatest {
//                println("s $it")
//                delay(240)
//                println(it)}}
//            println("time $t s")

            val s = flowOf("1", "2", "3").onEach { delay(300) }
            val ss = flowOf("1", "2", "3").onEach { delay(400) }
            s.zip(ss) { a, b -> "$a -> $b" }.collect { println(it) }
            s.combine(ss) { a, b -> "$a -> $b" }.collect { println(it) }
        }

        fun s2(s: Int): Flow<String> = flow {
            emit("$s ss")
            delay(700)
            emit("$s dd")
        }

        fun test16() = runBlocking {
            (1..4).asFlow().onEach { delay(100) }.flatMapMerge { s2(it) }.collect { println(it) }
            (1..4).asFlow().onEach { delay(200) }.flatMapConcat { s2(it) }.collect { println(it) }
            (1..4).asFlow().onEach { delay(200) }.flatMapLatest { s2(it) }.collect { println(it) }
        }

        fun ss2(): Flow<Int> = flow {
            for (i in 1..3) {
                println("---$i")
                emit(i)
            }
        }

        fun s3(): Flow<String> = flow {
            for (i in 1..3) {
                println("www $i")
                emit(i)
            }
        }.map {
            check(it <= 1) { "check $it" }
            "--- $it"
        }

        fun test17() = runBlocking {
//            try {
//                ss2().collect {
//                    println(it)
//                    check(it <= 1) { "----$it" }
//                }
//            } catch (e: Throwable) {
//                println(e)
//            }

//            try {
//                s3().collect {
//                    println(it)
//                }
//            } catch (e: Throwable) {
//                println(e)
//            }

//            s3().catch { emit("ee $it") }.collect{
//                println(it)
//            }

//            ss2().catch { println("eee $it") }.collect{ check(it<=1){"---- $it"}
//                println(it)
//            }

//            ss2().onEach {
//                check(it<=1){"---- $it"}
//                println(it)
//            }.catch { println("$it") }.collect()

//            try {
//                (1..3).asFlow().collect{ println(it)}
//            } finally {
//                println("end")
//            }

//            (1..3).asFlow().onCompletion { println("end") }.collect { println(it) }

//            flow {
//                emit(1)
//                throw RuntimeException()
//            }.onCompletion {
//                if (it != null) {
//                    println("flow")
//                }
//            }.catch { println("catch") }.collect { println(it) }

            (1..3).asFlow().onCompletion { println("end $it") }.collect {
                check(it <= 1) { "c $it" }
                println(it)
            }

        }

        fun test18() = runBlocking {
//            val job=GlobalScope.launch {
//                println("1111")
//                throw IndexOutOfBoundsException()
//            }
//            job.join()
//            println("-------")
//            val async = GlobalScope.async {
//                println("2222")
//                throw ArithmeticException()
//            }
//            try {
//                async.await()
//                println("33333")
//            } catch (e: Throwable) {
//                println(e)
//            }

            val handler = CoroutineExceptionHandler { _, e -> println(e) }

//            val job = GlobalScope.launch(handler) {
//                println("-----")
//                throw AssertionError()
//            }
//            val job2 = GlobalScope.async(handler) {
//                println("=======")
//                throw ArithmeticException()
//            }
//            joinAll(job, job2)

//            val job = launch{
//                val job2 = launch{
//                    try {
//                        delay(Long.MAX_VALUE)
//                    } catch (e: Throwable) {
//                        println("sssss $e")
//                    }
//                }
//                yield()
//                println("-----")
//                job2.cancel()
//                job2.join()
//                yield()
//                println("=====")
//            }
//            job.join()

//            val job = GlobalScope.launch(handler) {
//                launch {
//                    try {
//                        delay(Long.MAX_VALUE)
//                    } finally {
//                        withContext(NonCancellable) {
//                            println("ttttt")
//                            delay(20)
//                            println("rrrrrrr")
//
//                        }
//
//                    }
//                }
//                launch {
//                    delay(50)
//                    println("sssss")
//                    throw ArithmeticException()
//                }
//            }
//            job.join()

//            val job = GlobalScope.launch(handler){
//                launch {
//                    try {
//                        delay(Long.MAX_VALUE)
//                    } finally {
//                        throw ArithmeticException()
//                    }
//                }
//                launch {
//                    delay(200)
//                    throw IOException()
//                }
//                delay(Long.MAX_VALUE)
//            }
//            job.join()

            val job = GlobalScope.launch(handler) {
                val inn = launch {
                    launch {
                        launch {
                            throw IOException()
                        }
                    }
                }
                try {
                    inn.join()
                } catch (e: CancellationException) {
                    println(e)
                    throw e
                }
            }
            job.join()

        }

        fun test19() = runBlocking {
            val handler = CoroutineExceptionHandler { _, e -> println(e) }

//            val supervisor = SupervisorJob()
//            with(CoroutineScope(coroutineContext + supervisor)) {
//                val job = launch(handler) {
//                    println("11111")
//                    throw AssertionError("2222")
//                }
//                val job2 = launch {
//                    job.join()
//                    println("3333")
//                    try {
//                        delay(Long.MAX_VALUE)
//                    } finally {
//                        println("4444")
//                    }
//                }
//                job.join()
//                println("555")
//                supervisor.cancel()
//                job2.join()
//
//            }

//            try {
//                supervisorScope {
//                    val job = launch{
//                        try {
//                            println("111")
//                            delay(Long.MAX_VALUE)
//                        } finally {
//                            println("222")
//                        }
//                    }
//                    yield()
//                    println("333")
//                    throw AssertionError()
//                }
//            } catch (e: AssertionError) {
//                println(e)
//            }

            supervisorScope {
                launch(handler) {
                    println("111")
                    throw AssertionError()
                }
                println("222")
            }
            println("333")

        }

        suspend fun massiveRun(action: suspend () -> Unit) {
            val n = 100
            val k = 1000
            val time = measureTimeMillis {
                coroutineScope {
                    repeat(n) {
                        launch {
                            repeat(k) {
                                action()
                            }
                        }
                    }
                }
            }
            println("${n * k} ---> $time ms")

        }

        //        @Volatile
        var a = 0
        var b = AtomicInteger()
        val cont = newSingleThreadContext("cont")
        var c = 0
        var d = 0
        val mutex = Mutex()
        var e = 0
        fun test20() = runBlocking {
            withContext(Dispatchers.Default) {
                massiveRun {
                    a++
                }
            }
            println("a---> $a")

            withContext(Dispatchers.Default) {
                massiveRun {
                    b.incrementAndGet()
                }
            }
            println("b---> $b")

            withContext(Dispatchers.Default) {
                massiveRun {
                    withContext(cont) {
                        c++
                    }
                }
            }
            println("c---> $c")

            withContext(cont) {
                massiveRun {
                    d++
                }
            }
            println("d---> $d")

            withContext(Dispatchers.Default) {
                massiveRun {
                    mutex.withLock {
                        e++
                    }
                }
            }
            println("e---> $e")

        }

        fun CoroutineScope.ssss() = actor<Msg> {
            var s = 0
            for (m in channel) {
                when (m) {
                    is PutMsg -> s++
                    is GetMsg -> m.ress.complete(s)
                }
            }
        }

        fun test21() = runBlocking{
            val s = ssss()
            withContext(Dispatchers.Default){
                massiveRun {
                    s.send(PutMsg)
                }
            }
            val res = CompletableDeferred<Int>()
            s.send(GetMsg(res))
            println("---- ${res.await()}")
            s.close()
        }

        @JvmStatic
        fun main(args: Array<String>) {
            test21()
        }
    }

}

var a = 0

class Res {
    init {
        a++
    }

    fun close() {
        a--
    }
}

sealed class Msg
object PutMsg : Msg()
class GetMsg(val ress: CompletableDeferred<Int>) : Msg()