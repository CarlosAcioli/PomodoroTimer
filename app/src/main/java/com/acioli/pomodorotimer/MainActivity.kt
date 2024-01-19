package com.acioli.pomodorotimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.acioli.pomodorotimer.pomodoro_timer_service.presentation.time_tracker.PomodoroViewModel
import com.acioli.pomodorotimer.ui.theme.PomodoroTimerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.absoluteValue
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PomodoroTimerTheme {

                Greeting()

            }
        }
    }
}

@Composable
fun Greeting() {
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
                    mainTrackerViewModel.start(4.seconds, 4.seconds, 10.seconds, 2)
                }
            ) {
                Text(text = "Start")
            }

        }


    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Greeting()
}