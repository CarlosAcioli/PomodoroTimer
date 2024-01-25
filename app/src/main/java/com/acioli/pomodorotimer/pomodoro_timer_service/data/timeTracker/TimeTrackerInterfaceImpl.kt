package com.acioli.pomodorotimer.pomodoro_timer_service.data.timeTracker

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class TimeTrackerInterfaceImpl : TimerTrackerInterface {

    private val _focusState = MutableStateFlow<Duration>(0.seconds)
    val focusState = _focusState.asStateFlow()

    private val _shortPause = MutableStateFlow<Duration>(0.seconds)
    val shortPause = _shortPause.asStateFlow()

    private val _longPause = MutableStateFlow(0.seconds)
    val longPause = _longPause.asStateFlow()

    private val _cycle = MutableStateFlow(0)
    val cycle = _cycle.asStateFlow()

    private var isNotLongBreak = true

    private var job: Job = CoroutineScope(Dispatchers.IO).launch { }
    private var coroutineScope = CoroutineScope(Dispatchers.IO)

    override suspend fun start(
        focus: Duration,
        shortPause: Duration,
        longPause: Duration,
        cycles: Int
    ) {

        job.cancel()
        var initialTime = focus
        _focusState.value = initialTime
        job = coroutineScope.launch {

            if (isNotLongBreak) {

                repeat(cycles) {

                    _cycle.value = it + 1

                    while (initialTime > 0.seconds) {

//                        Log.d("Analysing", "focusState value loop: ${focusState.value.absoluteValue} ")
                        delay(1000L)
                        initialTime -= 1.seconds
                        _focusState.value = initialTime
//                        Log.d("Analysing", "focusState value loop: ${focusState.value.absoluteValue} ")

                    }

                    if (initialTime == 0.seconds) {

//                        _focusState.value = focus
                        var pause = shortPause
                        _shortPause.value = pause
//                        Log.d("Analysing", "pauseState value loop: ${this@TimeTrackerInterfaceImpl.shortPause.value.absoluteValue} ")

                        while (pause > 0.seconds) {
                            delay(1000L)
                            pause -= 1.seconds
                            _shortPause.value = pause
//                            Log.d("Analysing", "pauseState value loop: ${this@TimeTrackerInterfaceImpl.shortPause.value.absoluteValue} ")

                        }

                    }

                    initialTime = focus
                    _focusState.value = initialTime
//                    Log.d("Analysing", "focus state end cycle: ${focusState.value.absoluteValue} ")

                }

                isNotLongBreak = false
                start(0.minutes, shortPause, longPause, cycles)

            } else {

                var longBreak = longPause
                _longPause.value = longBreak
//                Log.d("Analysing", "longPause loop: ${this@TimeTrackerInterfaceImpl.longPause.value.absoluteValue} ")

                while (longBreak > 0.seconds) {
                    delay(1000L)
                    longBreak -= 1.seconds
                    _longPause.value = longBreak
//                    Log.d("Analysing", "longPause loop: ${this@TimeTrackerInterfaceImpl.longPause.value.absoluteValue} ")
                }

                isNotLongBreak = true

            }

        }

    }

    override fun reset() {
        job.cancel()
        isNotLongBreak = true
        _focusState.value = 0.seconds
        _shortPause.value = 0.seconds
        _longPause.value = 0.seconds
        _cycle.value = 0

    }

}
