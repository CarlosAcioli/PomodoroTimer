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
import com.acioli.pomodorotimer.pomodoro_timer_service.presentation.time_tracker.TimeTrackerViewModel
import com.acioli.pomodorotimer.ui.theme.PomodoroTimerTheme

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

        val vm = viewModel<TimeTrackerViewModel>()
        val state = vm.state.collectAsState()
        val pause = vm.pause.collectAsState()
        val longPause = vm.longPause.collectAsState()

        Column {

            Text(
                text = "Time is: ${state.value}",
                fontSize = 30.sp
            )

            Text(
                text = "Break: ${pause.value}"
            )

            Text(
                text = "Long break: ${longPause.value}"
            )

            Button(
                onClick = {
                }
            ) {
                Text(text = "Stop")
            }

            Button(
                onClick = {
                    vm.start()
                }
            ) {
                Text(text = "Start")
            }

            Button(
                onClick = {
                }
            ) {
                Text(text = "Resume")
            }

            Button(
                onClick = {
                }
            ) {
                Text(text = "Pause")
            }

        }


    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Greeting()
}