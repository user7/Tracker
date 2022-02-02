package com.gb.tracker.screens.tracker

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.gb.tracker.IAppScreens
import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import java.util.*


class TrackerPresenter(private val router: Router, private val screens: IAppScreens) :
    MvpPresenter<TrackerView>() {

    fun backPressed(): Boolean {
        router.exit()
        return true
    }

    data class TimerInterval(val startMs: Long, val endMs: Long)

    private var timerInterval: TimerInterval? = null
    private var intervalLengthMs: Long = 300_000
    private var committedIntervals = mutableListOf<TimerInterval>()
    private var nextSecondMs: Long = 0
    private var timer: Timer? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        setDisplay(intervalLengthMs)
    }

    private fun setDisplay(lengthMs: Long) {
        val secs = lengthMs / 1000 % 60
        val mins = lengthMs / 1000 / 60 % 60
        val hours = lengthMs / 1000 / 60 / 60 % 60
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
        nextSecondMs = ts
        timerInterval = TimerInterval(ts, ts + intervalLengthMs)
        viewState.showStart(false)
        viewState.showStop(true)
        viewState.showCommit(false)
        viewState.showDiscard(false)
        setTimerForNextSecond()
    }

    private fun setTimerForNextSecond() {
        val interval = timerInterval
        if (interval == null) {
            Log.d("==", "timer fired unexpectedly")
            return
        }

        val ts = System.currentTimeMillis()
        if (ts >= interval.endMs) {
            setDisplay(0)
            viewState.showStart(false)
            viewState.showStop(false)
            viewState.showCommit(true)
            viewState.showDiscard(true)
            viewState.playAlarm()
            return
        }

        if (ts >= nextSecondMs) {
            setDisplay(interval.endMs - nextSecondMs)
            nextSecondMs += 1000
        }

        Timer().apply { timer = this }.schedule(object : TimerTask() {
            override fun run() {
                Handler(Looper.getMainLooper()).post { setTimerForNextSecond() }
            }
        }, nextSecondMs - ts)
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