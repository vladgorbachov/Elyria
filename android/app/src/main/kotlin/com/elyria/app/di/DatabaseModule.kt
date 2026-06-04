package com.elyria.app.di

import android.content.Context
import androidx.room.Room
import com.elyria.app.core.constants.AppConstants
import com.elyria.app.data.local.dao.JournalDao
import com.elyria.app.data.local.dao.MoodDao
import com.elyria.app.data.local.database.AppDatabase
import com.elyria.app.data.local.database.MIGRATION_1_2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppConstants.DATABASE_NAME,
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    @Provides
    fun provideMoodDao(database: AppDatabase): MoodDao = database.moodDao()

    @Provides
    fun provideJournalDao(database: AppDatabase): JournalDao = database.journalDao()
}
