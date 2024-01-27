package com.acioli.pomodorotimer

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.acioli.pomodorotimer.pomodoro_timer_service.presentation.time_tracker.view.MainScreen
import com.acioli.pomodorotimer.ui.theme.PomodoroTimerTheme
import dagger.hilt.android.AndroidEntryPoint

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

                Greeting()

            }
        }

    }
}

@Composable
fun Greeting() {
    MainScreen()
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MainScreen()
}