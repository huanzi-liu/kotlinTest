package com.example.testdemo.http

import kotlinx.coroutines.*

interface CoroutineEvent {


    val coroutineScope: CoroutineScope

    val globalScope: CoroutineScope
        get() = GlobalScope

    val ioDispatcher: CoroutineDispatcher
        get() = Dispatchers.IO

    val mainDispatcher: CoroutineDispatcher
        get() = Dispatchers.Main

    val defaultDispatcher: CoroutineDispatcher
        get() = Dispatchers.Default

    suspend fun <T> withNonCancellable(block: suspend CoroutineScope.() -> T):T{
        return withContext(NonCancellable,block)
    }

    suspend fun <T> withMain(block: suspend CoroutineScope.() -> T):T{
        return withContext(mainDispatcher,block)
    }

    suspend fun <T> withIo(block: suspend CoroutineScope.() -> T):T{
        return withContext(ioDispatcher,block)
    }

    suspend fun <T> withDefault(block: suspend CoroutineScope.() -> T):T{
        return withContext(defaultDispatcher,block)
    }


    fun launchMain(block: suspend CoroutineScope.() -> Unit): Job {
        return coroutineScope.launch(context = mainDispatcher, block = block)
    }

    fun launchIo(block: suspend CoroutineScope.() -> Unit): Job{
        return coroutineScope.launch (context = ioDispatcher,block = block)
    }

    fun launchDefault(block: suspend CoroutineScope.() -> Unit):Job{
        return coroutineScope.launch(context = defaultDispatcher,block = block)
    }

}