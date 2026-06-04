package com.mindease.app.di

import com.mindease.app.data.ai.AICompanion
import com.mindease.app.data.ai.MockAICompanion
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {
    @Binds
    @Singleton
    abstract fun bindAICompanion(impl: MockAICompanion): AICompanion
}
