package com.acioli.pomodorotimer.pomodoro_timer_service.domain.service

import android.Manifest
import android.app.PendingIntent
import android.app.Service
import android.app.TaskStackBuilder
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Binder
import android.os.IBinder
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.acioli.pomodorotimer.MainActivity
import com.acioli.pomodorotimer.R
import com.acioli.pomodorotimer.pomodoro_timer_service.data.timeTracker.TimeTrackerInterfaceImpl
import com.acioli.pomodorotimer.pomodoro_timer_service.presentation.time_tracker.PomodoroViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes


@AndroidEntryPoint
class PomodoroTimerService : Service() {

    @Inject
    lateinit var pendingIntent: PendingIntent

    private val timeTracker = TimeTrackerInterfaceImpl()

//    private val binder = LocalBinder()

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when (intent?.action) {

            Actions.START.toString() -> {

                val focus = intent.getIntExtra("focus",0).minutes
                val short = intent.getIntExtra("short",0).minutes
                val long = intent.getIntExtra("long",0).minutes
                val cycle = intent.getIntExtra("cycle",0)

                start(focus, short, long, cycle)

            }

            Actions.STOP.toString() -> {
                stop()
                stopSelf()
                CoroutineScope(Dispatchers.IO).launch { timeTracker.reset() }
            }

        }

        return super.onStartCommand(intent, flags, startId)
    }

//    var focusTime = MutableStateFlow(0L)
//        private set
//    var pauseTime = MutableStateFlow(0L)
//        private set
//    var cycleIndicator = MutableStateFlow(0)
//        private set
//    var longPauseTime = MutableStateFlow(0L)
//        private set

    private fun start(focus: Duration, shortPause: Duration, longPause: Duration, cycles: Int) {
        val notificationManager = NotificationManagerCompat.from(this)
        val notification = NotificationCompat.Builder(this, "pomodoro_timer").apply {
            setSmallIcon(R.drawable.tomate)
            setContentTitle("Pomodoro running")
            setContentInfo("Timer")
            setContentIntent(pendingIntent)
            setAutoCancel(true)
        }

//        startForeground(1, notification.build())

        CoroutineScope(Dispatchers.IO).launch {
            timeTracker.start(focus, shortPause, longPause, cycles)

            launch {
                timeTracker.focusState.collect { focus ->
                    notification.setContentText("Focus time: ${focus.absoluteValue}")
//                    focusTime.value = focus.inWholeSeconds
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
//                    pauseTime.value = pause.inWholeSeconds
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
//                    longPauseTime.value = longPause.inWholeSeconds
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

//    inner class LocalBinder: Binder() {
//        fun getService(): PomodoroTimerService = this@PomodoroTimerService
//    }

    private fun stop() {

        val notificationManager = NotificationManagerCompat.from(this)

        notificationManager.cancelAll()

    }

    enum class Actions {
        START, STOP
    }

}