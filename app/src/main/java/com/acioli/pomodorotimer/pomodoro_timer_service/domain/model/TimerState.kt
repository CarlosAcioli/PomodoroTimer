package com.acioli.pomodorotimer.pomodoro_timer_service.domain.model

import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class TimerState(
    var focusTime: Duration = 0.seconds,
    var shortPause: Duration = 0.seconds,
    var longPause: Duration = 0.seconds
)
