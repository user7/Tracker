package com.gb.tracker.screens.tracker

import android.util.Log
import com.gb.tracker.IAppScreens
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import moxy.MvpPresenter
import java.util.concurrent.TimeUnit


class TrackerPresenter(private val router: Router, private val screens: IAppScreens) :
    MvpPresenter<TrackerView>() {

    fun backPressed(): Boolean {
        router.exit()
        return true
    }

    data class TimerInterval(val startMs: Long, val endMs: Long)

    private var timerInterval: TimerInterval? = null
    private var intervalLengthMs: Long = 5_000
    private var committedIntervals = mutableListOf<TimerInterval>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        setDisplay(intervalLengthMs)
    }

    private fun setDisplay(lengthMs: Long) {
        val secs = lengthMs / 1000 % 60
        val mins = lengthMs / 1000 / 60 % 60
        val hours = lengthMs / 1000 / 60 / 60
        val display = when {
            hours > 0 -> "%d:%02d:%02d".format(hours, mins, secs)
            mins > 0 -> "%d:%02d".format(mins, secs)
            else -> "0:%02d".format(secs)
        }
        viewState.setDisplay(display)
    }

    fun editEntitiesButtonPressed() {
        router.navigateTo(screens.entities())
    }

    fun startPressed() {
        val ts = System.currentTimeMillis()
        val thisInterval = TimerInterval(ts, ts + intervalLengthMs)
        timerInterval = thisInterval
        viewState.showStart(false)
        viewState.showStop(true)
        viewState.showCommit(false)
        viewState.showDiscard(false)

        var disposable: Disposable? = null
        disposable = Observable.interval(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { tick ->
                    val curTs = System.currentTimeMillis()
                    val dts = curTs - ts
                    Log.d("==", "second $tick dts=${dts / 1000.0}")

                    // таймер был перезапущен или сброщен, завершить этот поток
                    if (thisInterval !== timerInterval) {
                        disposable?.dispose()
                        return@subscribe
                    }

                    // время вышло
                    if (curTs >= thisInterval.endMs) {
                        setDisplay(0)
                        viewState.showStart(false)
                        viewState.showStop(false)
                        viewState.showCommit(true)
                        viewState.showDiscard(true)
                        viewState.playAlarm()
                        disposable?.dispose()
                        return@subscribe
                    }

                    // просто обновить дисплей
                    setDisplay((thisInterval.endMs - curTs + 500) / 1000 * 1000)
                },
                { error ->
                    Log.d("==", "exception: ${error.message}")
                }
            )
    }

    fun stopPressed() {
        timerInterval = null
        setDisplay(intervalLengthMs)
        viewState.showStart(true)
        viewState.showStop(false)
        viewState.showCommit(false)
        viewState.showDiscard(false)
    }

    fun commitPressed() {
        timerInterval?.let { committedIntervals.add(it) }
        timerInterval = null
        startPressed()
    }

    fun discardPressed() {
        timerInterval = null
        startPressed()
    }
}