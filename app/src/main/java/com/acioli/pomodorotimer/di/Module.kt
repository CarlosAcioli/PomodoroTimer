package com.acioli.pomodorotimer.di

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import com.acioli.pomodorotimer.MainActivity
import com.acioli.pomodorotimer.R
import com.acioli.pomodorotimer.pomodoro_timer_service.data.timeTracker.TimeTrackerInterfaceImpl
import com.acioli.pomodorotimer.pomodoro_timer_service.data.timeTracker.TimerTrackerInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
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
    fun provideIntent(@ApplicationContext context: Context): PendingIntent {

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        return PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    @Provides
    fun provideMusicPlayer(@ApplicationContext context: Context): MediaPlayer {
        return MediaPlayer.create(context, R.raw.end_timer)
    }

    @Provides
    @Named("StopSound")
    fun provideStopSound(@ApplicationContext context: Context): MediaPlayer {
        return MediaPlayer.create(context, R.raw.pause_timer)
    }

    @Provides
    @Named("StartSound")
    fun provideStartSound(@ApplicationContext context: Context): MediaPlayer {
        return MediaPlayer.create(context, R.raw.start_timer)
    }

}