package com.gb.tracker.screens.entities

import com.gb.tracker.IAppScreens
import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter

class EntitiesPresenter(private val router: Router, private val screens: IAppScreens) :
    MvpPresenter<EntitiesView>() {

    fun backPressed(): Boolean {
        router.backTo(screens.tracker())
        return true
    }

}