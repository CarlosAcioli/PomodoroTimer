package com.acioli.pomodorotimer

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.acioli.pomodorotimer.pomodoro_timer_service.domain.service.PomodoroTimerService
import com.acioli.pomodorotimer.pomodoro_timer_service.presentation.time_tracker.PomodoroViewModel
import com.acioli.pomodorotimer.ui.theme.PomodoroTimerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                0
            )

        }
        setContent {
            PomodoroTimerTheme {

                Greeting(this)

            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(context: Context) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        val mainTrackerViewModel = viewModel<PomodoroViewModel>()
        val mainTimerState = mainTrackerViewModel.state.collectAsState()
        val shortPauseState = mainTrackerViewModel.pause.collectAsState()
        val longPauseSate = mainTrackerViewModel.longPause.collectAsState()
        val cycleState = mainTrackerViewModel.cycleState.collectAsState()

        val sheetState = rememberModalBottomSheetState()
        var isSheetOpen by remember { mutableStateOf(false) }

        Column {

            Text(
                text = "Time is: ${mainTimerState.value.absoluteValue}",
                fontSize = 30.sp
            )

            Text(
                text = "Break is: ${shortPauseState.value.absoluteValue}"
            )


            Text(
                text = "Long break: ${longPauseSate.value.absoluteValue}"
            )

            Text(
                text = "Cycle: ${cycleState.value}"
            )

            Button(
                onClick = {
                    mainTrackerViewModel.stop()
                }
            ) {
                Text(text = "Stop")
            }

            Button(
                onClick = {
                    isSheetOpen = true
                }
            ) {
                Text(text = "Choose pomodoro timer")
            }

            Button(
                onClick = {
                    mainTrackerViewModel.start(4.seconds, 4.seconds, 10.seconds, 2)

                    Intent(context, PomodoroTimerService::class.java).also {
                        val focus = 4
                        val shortPause = 4
                        val longPause = 10
                        val cycle = 2
                        it.action = PomodoroTimerService.Actions.START.toString()
                        it.putExtra("focus", focus)
                        it.putExtra("short", shortPause)
                        it.putExtra("long", longPause)
                        it.putExtra("cycle", cycle)
                        context.startService(it)
                    }
                }
            ) {
                Text(text = "Start")
            }

            Button(
                onClick = {
                    Intent(context, PomodoroTimerService::class.java).also {
                        it.action = PomodoroTimerService.Actions.START.toString()
                        context.startService(it)
                    }
                }
            ) {
                Text(text = "Foreground service on")
            }

            Button(
                onClick = {
                    Intent(context, PomodoroTimerService::class.java).also {
                        it.action = PomodoroTimerService.Actions.STOP.toString()
                        context.startService(it)
                    }
                }
            ) {
                Text(text = "Foreground service off")
            }

        }

        if (isSheetOpen) {
            ModalBottomSheet(
                onDismissRequest = {
                    isSheetOpen = false
                },
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(text = "Focus timer:")
                    Text(text = "Short Pause:")
                    Text(text = "Long Pause after cycle:")
                    Text(text = "Cycles:")
                }
            }
        }


    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
}