package com.gb.tracker.screens.tracker

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface TrackerView : MvpView {
    fun setDisplay(timer: String)
    fun showStart(show: Boolean)
    fun showStop(show: Boolean)
    fun showCommit(show: Boolean)
    fun showDiscard(show: Boolean)
    fun playAlarm()
}
