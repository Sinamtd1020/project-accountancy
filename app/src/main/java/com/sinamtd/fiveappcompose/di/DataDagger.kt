package com.sinamtd.fiveappcompose.di

import android.content.Context
import androidx.room.Room
import com.sinamtd.fiveappcompose.model.data.MyDataBase
import com.sinamtd.fiveappcompose.model.data.dao.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

object DataDagger {
    @Module
    @InstallIn(SingletonComponent::class)
    object DatabaseModule {

        @Provides
        @Singleton
        fun provideMyDatabase(@ApplicationContext context: Context): MyDataBase{
            return Room.databaseBuilder(
                context,
                MyDataBase::class.java,
                MyDataBase.DB_NAME
            ).build()
        }
        @Provides
        fun provideDao(myDataBase: MyDataBase): TransactionDao{
            return myDataBase.transactionDao()
        }

    }
}