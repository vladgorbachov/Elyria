package com.mindease.app.di

import com.mindease.app.data.repository.JournalRepositoryImpl
import com.mindease.app.data.repository.MoodRepositoryImpl
import com.mindease.app.data.repository.PracticeRepositoryImpl
import com.mindease.app.data.repository.SettingsRepositoryImpl
import com.mindease.app.domain.repository.JournalRepository
import com.mindease.app.domain.repository.MoodRepository
import com.mindease.app.domain.repository.PracticeRepository
import com.mindease.app.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindMoodRepository(impl: MoodRepositoryImpl): MoodRepository

    @Binds
    @Singleton
    abstract fun bindJournalRepository(impl: JournalRepositoryImpl): JournalRepository

    @Binds
    @Singleton
    abstract fun bindPracticeRepository(impl: PracticeRepositoryImpl): PracticeRepository
}
