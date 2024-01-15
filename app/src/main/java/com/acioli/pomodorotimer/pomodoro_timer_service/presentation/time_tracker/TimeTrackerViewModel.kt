package com.acioli.pomodorotimer.pomodoro_timer_service.presentation.time_tracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class TimeTrackerViewModel : ViewModel() {

    private val _state = MutableStateFlow<String>("")
    val state = _state.asStateFlow()

    private val _pause = MutableStateFlow<String>("")
    val pause = _pause.asStateFlow()

    private val _longPause = MutableStateFlow("")
    val longPause = _longPause.asStateFlow()

    private var flow: Flow<Duration> = emptyFlow()
    private var isNotLongBreak = true

    private var coroutineScope = viewModelScope

    fun start() {

        coroutineScope.launch {

            flow = flow {

                var initialValue = 4.seconds
                _state.value = initialValue.toString()
                emit(initialValue)


                if (isNotLongBreak) {

                    repeat(2) {

                        println("repeating $it times")

                        while (initialValue > 0.seconds) {

                            delay(1000L)
                            initialValue -= 1.seconds
                            emit(initialValue)
                            _state.value = initialValue.toString()

                        }

                        if (initialValue == 0.seconds) {

                            var pause = 4.seconds
                            _pause.value = pause.toString()

                            while (pause > 0.seconds) {
                                delay(1000L)
                                pause -= 1.seconds
                                _pause.value = pause.toString()
                            }

                        }

                        initialValue = 4.seconds
                        _state.value = initialValue.toString()
                        emit(initialValue)

                    }

                    isNotLongBreak = false
                    start()

                } else {

                    var longBreak = 10.seconds
                    _longPause.value = longBreak.toString()

                    while (longBreak > 0.seconds) {
                        delay(1000L)
                        longBreak -= 1.seconds
                        _longPause.value = longBreak.toString()
                    }

                    isNotLongBreak = true

                }

            }

            coroutineScope.launch {
                flow.collect {
                    _state.value = it.toString()
                }
            }

        }

    }


}

fun reset() {

}

fun pause() {

}



