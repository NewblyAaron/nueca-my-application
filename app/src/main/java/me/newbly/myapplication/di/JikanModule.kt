package me.newbly.myapplication.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.newbly.myapplication.api.JikanService

@InstallIn(SingletonComponent::class)
@Module
class JikanModule {
    @Provides
    fun provideService(): JikanService = JikanService.create()
}