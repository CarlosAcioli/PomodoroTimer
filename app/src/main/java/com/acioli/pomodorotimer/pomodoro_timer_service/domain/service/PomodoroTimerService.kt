package com.acioli.pomodorotimer.pomodoro_timer_service.domain.service

import android.Manifest
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.acioli.pomodorotimer.R
import com.acioli.pomodorotimer.pomodoro_timer_service.data.timeTracker.TimeTrackerInterfaceImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class PomodoroTimerService : Service() {

    private val timeTracker = TimeTrackerInterfaceImpl()

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when (intent?.action) {

            Actions.START.toString() -> {

                val focus = intent.getIntExtra("focus",0).seconds
                val short = intent.getIntExtra("short",0).seconds
                val long = intent.getIntExtra("long",0).seconds
                val cycle = intent.getIntExtra("cycle",0)

                start(focus, short, long, cycle)

            }
            Actions.STOP.toString() -> CoroutineScope(Dispatchers.IO).launch { timeTracker.reset() }

        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun start(focus: Duration, shortPause: Duration, longPause: Duration, cycles: Int) {
        val max = 4
        var current = 0
        val notificationManager = NotificationManagerCompat.from(this)
        val notification = NotificationCompat.Builder(this, "pomodoro_timer").apply {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentTitle("Pomodoro timer")
            setContentInfo("Running")
        }

        CoroutineScope(Dispatchers.IO).launch {
            timeTracker.start(focus, shortPause, longPause, cycles)

            launch {
                timeTracker.focusState.collect { focus ->
                    notification.setContentText("Focus time: ${focus.absoluteValue}")
                    if (ActivityCompat.checkSelfPermission(
                            this@PomodoroTimerService,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return@collect
                    }
                    notificationManager.notify(1, notification.build())
                }
            }

            launch {
                timeTracker.shortPause.collect { pause ->
                    notification.setContentText("Pause time: ${pause.absoluteValue}")
                    if (ActivityCompat.checkSelfPermission(
                            this@PomodoroTimerService,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return@collect
                    }
                    notificationManager.notify(1, notification.build())
                }
            }

            launch {
                timeTracker.longPause.collect { longPause ->
                    notification.setContentText("Long pause: ${longPause.absoluteValue}")
                    if (ActivityCompat.checkSelfPermission(
                            this@PomodoroTimerService,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return@collect
                    }
                    notificationManager.notify(1, notification.build())
                }
            }

        }

    }

    enum class Actions {
        START, STOP
    }

    suspend fun durations(focus: Duration, shortPause: Duration, longPause: Duration, cycles: Int) {
        timeTracker.start(focus, shortPause, longPause, cycles)
    }

}