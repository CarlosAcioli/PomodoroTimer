package com.acioli.pomodorotimer

import android.Manifest
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import com.acioli.pomodorotimer.pomodoro_timer_service.domain.service.PomodoroTimerService
import com.acioli.pomodorotimer.pomodoro_timer_service.presentation.time_tracker.view.MainScreen
import com.acioli.pomodorotimer.ui.theme.PomodoroTimerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var mService: PomodoroTimerService
    private var isBound = mutableStateOf(false)

    private val connection = object: ServiceConnection {
        override fun onServiceConnected(className: ComponentName?, service: IBinder) {
            val binder = service as PomodoroTimerService.LocalBinder
            mService = binder.getService()
            isBound.value = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isBound.value = false
        }

    }

    @SuppressLint("StateFlowValueCalledInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TAG", "onCreate: OnCreate")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
        setContent {
            PomodoroTimerTheme {

                val pref = getSharedPreferences("userData", Context.MODE_PRIVATE)
                Log.d("TAG", "onCreate: ${pref.getString("focusDuration", "")}")
                Log.d("TAG", "onCreate: ${pref.getString("shortPauseDuration", "")}")
                Log.d("TAG", "onCreate: ${pref.getString("cycleUntilLongPause", "")}")
                Log.d("TAG", "onCreate: ${pref.getString("longPauseDuration", "")}")

                if(isBound.value){

                    MainScreen(
                        this,
                        mService,
                        pref.getString("focusDuration", "")!!,
                        pref.getString("shortPauseDuration", "")!!,
                        pref.getString("cycleUntilLongPause", "")!!,
                        pref.getString("longPauseDuration", "")!!
                    )

                }

                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

            }
        }

    }

    override fun onStart() {
        super.onStart()
        Log.d("TAG", "onCreate: OnStart")

        Intent(this, PomodoroTimerService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }

    }

    override fun onResume() {
        super.onResume()
        Log.d("TAG", "onCreate: OnResume")
    }

    override fun onStop() {
        super.onStop()
        Log.d("TAG", "onCreate: OnStop")

//        unbindService(connection)
//        isBound = false

    }
}