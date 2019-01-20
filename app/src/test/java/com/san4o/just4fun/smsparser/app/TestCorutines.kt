package com.san4o.just4fun.smsparser.app

import kotlinx.coroutines.*
import org.junit.Test

class TestCorutines {

    @Test
    fun ScopeBuilder() = runBlocking {
        launch {
            delay(200L)
            println("2. Task from runBlocking")
        }

        coroutineScope {
            // Creates a new coroutine scope
            launch {
                delay(500L)
                println("3. Task from nested launch")
            }

            delay(100L)
            println("1. Task from coroutine scope") // This line will be printed before nested launch
        }

        println("4. Coroutine scope is over") // This line is not printed until nested launch completes
    }

    @Test
    fun extractFunctionRefactoring() = runBlocking {
        launch { doWorld() }
        println("Hello,")
    }

    // this is your first suspending function
    private suspend fun doWorld() {
        delay(1000L)
        println("World!")
    }

    @Test
    fun  lightWeightMain() = runBlocking {
        repeat(100_000) { // launch a lot of coroutines

            launch {
                delay(1000L)
                print(".")

            }
        }
    }

    @Test
    fun daemonThreadsMain() = runBlocking {
        //sampleStart
        GlobalScope.launch {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
        }
        delay(1300L) // just quit after delay
//sampleEnd
    }

    @Test
    fun cancellingCoroutineExecutionMain() = runBlocking {
        //sampleStart
        val job = launch {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
        }
        delay(1300L) // delay a bit
        println("main: I'm tired of waiting!")
//        job.cancel() // cancels the job
//        job.join() // waits for job's completion
        job.cancelAndJoin()
        println("main: Now I can quit.")
            //sampleEnd
    }

    @Test
    fun cancellationIsCooperativeMain() = runBlocking {
        //sampleStart
        val startTime = System.currentTimeMillis()
        val job = launch(Dispatchers.Default) {
            var nextPrintTime = startTime
            var i = 0
            while (i < 5) { // computation loop, just wastes CPU
                // print a message twice a second
                if (System.currentTimeMillis() >= nextPrintTime) {
                    println("I'm sleeping ${i++} ...")
                    nextPrintTime += 500L
                }
            }
        }
        delay(1300L) // delay a bit
        println("main: I'm tired of waiting!")
        job.cancelAndJoin() // cancels the job and waits for its completion
        println("main: Now I can quit.")
//sampleEnd
    }

    @Test
    fun makingComputationCodeCancellable() = runBlocking {
//sampleStart
        val startTime = System.currentTimeMillis()
        val job = launch(Dispatchers.Default) {
            var nextPrintTime = startTime
            var i = 0
            while (isActive) { // cancellable computation loop
                // print a message twice a second
                if (System.currentTimeMillis() >= nextPrintTime) {
                    println("I'm sleeping ${i++} ...")
                    nextPrintTime += 500L
                }
            }
        }
        delay(1300L) // delay a bit
        println("main: I'm tired of waiting!")
        job.cancelAndJoin() // cancels the job and waits for its completion
        println("main: Now I can quit.")
//sampleEnd
    }
    @Test
    fun closingResourcesWithFinally() = runBlocking {
///sampleStart
        val job = launch {
            try {
                repeat(1000) { i ->
                    println("I'm sleeping $i ...")
                    delay(500L)
                }
            } catch (e : Exception){
                println("catch ${e.javaClass.simpleName}")
            }finally {
                println("I'm running finally")
            }
        }
        delay(1300L) // delay a bit
        println("main: I'm tired of waiting!")
        job.cancelAndJoin() // cancels the job and waits for its completion
        println("main: Now I can quit.")
//sampleEnd
    }
    @Test
    fun runNonCancellableBlock() = runBlocking {
        //sampleStart
        val job = launch {
            try {
                repeat(1000) { i ->
                    println("I'm sleeping $i ...")
                    delay(500L)
                }
            } finally {
                withContext(NonCancellable) {
                    println("I'm running finally")
                    delay(1000L)
                    println("And I've just delayed for 1 sec because I'm non-cancellable")
                }
            }
        }
        delay(1300L) // delay a bit
        println("main: I'm tired of waiting!")
        job.cancelAndJoin() // cancels the job and waits for its completion
        println("main: Now I can quit.")
//sampleEnd
    }
    @Test
    fun timeout() = runBlocking {
        //sampleStart
        withTimeout(1300L) {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
        }
//sampleEnd
    }
    @Test
    fun timeout2() = runBlocking {
        //sampleStart
        val result = withTimeoutOrNull(1300L) {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
            "Done" // will get cancelled before it produces this result
        }
        println("Result is $result")
//sampleEnd
    }
}