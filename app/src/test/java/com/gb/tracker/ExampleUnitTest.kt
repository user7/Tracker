package com.gb.tracker

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import org.junit.Test

import org.junit.Assert.*
import java.lang.IllegalArgumentException
import java.util.concurrent.TimeUnit

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun rxJavaStream() {
        rxJavaStream(false)
        rxJavaStream(true)
    }

    fun rxJavaStream(withError: Boolean) {
        val o = Observable.create<String> { emitter ->
            emitter.onNext("hello")
            emitter.onNext("world")
            if (withError) {
                emitter.onError(IllegalArgumentException("error happened"))
            }
            emitter.onComplete()
        }
        o.subscribe(
            { text ->
                println("event: $text")
            },
            { error ->
                println("error: ${error.message}")
            },
            {
                println("completed")
            }
        )
        Thread.sleep(200)
    }

    @Test
    fun rxJavaDisposable() {
        val o = Observable.interval(100, TimeUnit.MILLISECONDS)
        var dis: Disposable? = null
        dis = o.subscribe { tick ->
            if (tick > 3) {
                dis?.dispose()
            }
            println("tick $tick")
        }
        Thread.sleep(1000)
    }

    @Test
    fun rxJavaFlatMap() {
        var out: Long = 0
        Observable
            .just(1, 20, 300)
            .flatMap { x -> Observable.just(x, x * 1000).delay(10, TimeUnit.MILLISECONDS) }
            .subscribe { x -> out += x }
        Thread.sleep(100)
        assertEquals(321321, out)
    }

    @Test
    fun rxJavaSwitchMap() {
        var out: Long = 0
        Observable
            .just(1, 20, 300)
            .switchMap { x -> Observable.just(x, x * 1000).delay(10, TimeUnit.MILLISECONDS) }
            .subscribe { x -> out += x }
        Thread.sleep(100)
        assertEquals(300300, out)
    }
}