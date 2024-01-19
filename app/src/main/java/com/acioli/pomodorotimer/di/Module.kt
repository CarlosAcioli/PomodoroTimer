package com.acioli.pomodorotimer.di

import com.acioli.pomodorotimer.pomodoro_timer_service.data.timeTracker.TimeTrackerInterfaceImpl
import com.acioli.pomodorotimer.pomodoro_timer_service.data.timeTracker.TimerTrackerInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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



}