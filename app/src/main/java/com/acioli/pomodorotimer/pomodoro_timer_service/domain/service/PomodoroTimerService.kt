package com.acioli.pomodorotimer.pomodoro_timer_service.domain.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.acioli.pomodorotimer.R
import com.acioli.pomodorotimer.pomodoro_timer_service.data.timeTracker.TimeTrackerInterfaceImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds


@AndroidEntryPoint
class PomodoroTimerService : Service() {

    @Inject
    lateinit var pendingIntent: PendingIntent

    @Inject
    lateinit var mpPause: MediaPlayer

    @Inject
    @Named("StartSound")
    lateinit var mpStart: MediaPlayer

    @Inject
    @Named("StopSound")
    lateinit var mpStop: MediaPlayer

    private val timeTracker = TimeTrackerInterfaceImpl()

    private val binder = LocalBinder()

    override fun onBind(p0: Intent?): IBinder? {
        return binder
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

    var focusTime = MutableStateFlow(0.seconds)
        private set
    var pauseTime = MutableStateFlow(0.seconds)
        private set
    var cycleIndicator = MutableStateFlow(0)
        private set
    var longPauseTime = MutableStateFlow(0.seconds)
        private set


    @SuppressLint("WrongConstant")
    private fun start(focus: Duration, shortPause: Duration, longPause: Duration, cycles: Int) {
        val notificationManager = NotificationManagerCompat.from(this)
        val notification = NotificationCompat.Builder(this, "pomodoro_timer").apply {
            setSmallIcon(R.drawable.tomate)
            setContentTitle("Pomodoro running")
            setContentInfo("Timer")
            setContentIntent(pendingIntent)
            setAutoCancel(true)
            setOngoing(false)
        }

        startForeground(1, notification.build())

        CoroutineScope(Dispatchers.IO).launch {
            timeTracker.start(focus, shortPause, longPause, cycles)

            launch {
                timeTracker.focusState.collect { focus ->
                    notification.setContentText("Focus time: ${focus.absoluteValue}")
                    focusTime.value = focus

                    if (focusTime.value.absoluteValue == 1.seconds) {
                        mpPause.start()
                    }

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
                    pauseTime.value = pause

                    if(pauseTime.value == 1.seconds){
                        mpStart.start()
                    }

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
                    longPauseTime.value = longPause

                    if(longPauseTime.value == 1.seconds){
                        mpStop.start()
                    }

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
                timeTracker.cycle.collect{
                    cycleIndicator.value = it
                }
            }

        }

    }

    inner class LocalBinder: Binder() {
        fun getService(): PomodoroTimerService = this@PomodoroTimerService
    }

    private fun stop() {

        val notificationManager = NotificationManagerCompat.from(this)

        notificationManager.cancelAll()

    }

    enum class Actions {
        START, STOP
    }

}