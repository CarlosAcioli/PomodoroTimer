package com.acioli.pomodorotimer.pomodoro_timer_service.domain.model

import kotlin.time.Duration

data class TimerStateFlow (
    var focusTime: Duration,
    var shortPause: Duration,
    var longPause: Duration
)