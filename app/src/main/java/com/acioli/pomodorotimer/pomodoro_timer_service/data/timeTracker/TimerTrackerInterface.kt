package com.acioli.pomodorotimer.pomodoro_timer_service.data.timeTracker

import kotlin.time.Duration

interface TimerTrackerInterface {

    suspend fun start(focus: Duration, shortPause: Duration, longPause: Duration, cycles: Int)

    fun reset()

}