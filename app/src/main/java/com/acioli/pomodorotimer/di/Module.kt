package com.acioli.pomodorotimer.di

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.acioli.pomodorotimer.R
import com.acioli.pomodorotimer.pomodoro_timer_service.data.timeTracker.TimeTrackerInterfaceImpl
import com.acioli.pomodorotimer.pomodoro_timer_service.data.timeTracker.TimerTrackerInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun provideTimerTracker(): TimerTrackerInterface {
        return TimeTrackerInterfaceImpl()
    }

    @Provides
    fun provideNotificationBuilder(@ApplicationContext context: Context): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, "pomodoro_timer").apply {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentTitle("Pomodoro timer")
            setContentInfo("Running")
        }
    }

    @Provides
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManagerCompat{
        return NotificationManagerCompat.from(context)
    }

}