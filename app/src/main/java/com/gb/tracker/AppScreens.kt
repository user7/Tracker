package com.gb.tracker

import com.gb.tracker.screens.entities.EntitiesFragment
import com.gb.tracker.screens.tracker.TrackerFragment
import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.FragmentScreen

class AppScreens : IAppScreens {
    override fun tracker(): Screen = FragmentScreen { TrackerFragment.newInstance() }

    override fun entities(): Screen = FragmentScreen { EntitiesFragment.newInstance() }
}