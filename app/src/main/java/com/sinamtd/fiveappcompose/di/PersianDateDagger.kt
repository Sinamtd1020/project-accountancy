package com.sinamtd.fiveappcompose.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.Date
import java.util.GregorianCalendar

@Module
@InstallIn(SingletonComponent::class)
object PersianDateDagger {

    @Provides
    fun provideDate(): Date= Date()

    @Provides
    fun provideCalender(date: Date): GregorianCalendar=
        GregorianCalendar().apply {
            time=date
        }
}