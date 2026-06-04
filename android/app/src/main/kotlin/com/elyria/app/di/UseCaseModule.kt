package com.elyria.app.di

import com.elyria.app.data.ai.MlKitLanguageIdentifier
import com.elyria.app.data.ai.MockAICompanion
import com.elyria.app.domain.ai.AICompanion
import com.elyria.app.domain.ai.LanguageIdentifier
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

    @Binds
    @Singleton
    abstract fun bindLanguageIdentifier(impl: MlKitLanguageIdentifier): LanguageIdentifier
}
