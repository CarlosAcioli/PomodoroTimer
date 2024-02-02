package com.acioli.pomodorotimer

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import com.acioli.pomodorotimer.pomodoro_timer_service.presentation.time_tracker.view.MainScreen
import com.acioli.pomodorotimer.ui.theme.PomodoroTimerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

//    private lateinit var mService: PomodoroTimerService
//    private var isBound by mutableStateOf(false)
//
//    private val connection = object: ServiceConnection {
//        override fun onServiceConnected(className: ComponentName?, service: IBinder) {
//            val binder = service as PomodoroTimerService.LocalBinder
//            mService = binder.getService()
//            isBound = true
//        }
//
//        override fun onServiceDisconnected(p0: ComponentName?) {
//            isBound = false
//        }
//
//    }

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

//                if(isBound){
//
//                }

                MainScreen(this)

                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

            }
        }

    }

    override fun onStart() {
        super.onStart()
        Log.d("TAG", "onCreate: OnStart")

//        Intent(this, PomodoroTimerService::class.java).also { intent ->
//            bindService(intent, connection, Context.BIND_AUTO_CREATE)
//        }

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