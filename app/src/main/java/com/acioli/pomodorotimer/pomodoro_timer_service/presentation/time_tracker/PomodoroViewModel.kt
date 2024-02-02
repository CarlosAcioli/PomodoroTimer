package com.acioli.pomodorotimer.pomodoro_timer_service.presentation.time_tracker

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acioli.pomodorotimer.pomodoro_timer_service.data.timeTracker.TimeTrackerInterfaceImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class PomodoroViewModel : ViewModel() {

    private val _state = MutableStateFlow<Duration>(0.minutes)
    val state = _state.asStateFlow()

    private val _pause = MutableStateFlow<Duration>(0.minutes)
    val pause = _pause.asStateFlow()

    private val _longPause = MutableStateFlow(0.minutes)
    val longPause = _longPause.asStateFlow()

    private val _cycleState = MutableStateFlow(0)
    val cycleState = _cycleState.asStateFlow()

    private val timeTracker = TimeTrackerInterfaceImpl()

    fun start(focus: Duration, shortPause: Duration, longPause: Duration, cycles: Int) {

        viewModelScope.launch {

            timeTracker.start(focus, shortPause, longPause, cycles)

            launch {
                timeTracker.focusState.collect {

                    _state.value = it

                }
            }

            launch {
                timeTracker.shortPause.collect {
                    _pause.value = it

                }
            }

            launch {
                timeTracker.longPause.collect {
                    _longPause.value = it

                }
            }

            launch {
                timeTracker.cycle.collect {
                    _cycleState.value = it
                }
            }

        }

    }

    fun stop() {
        timeTracker.reset()
    }

}