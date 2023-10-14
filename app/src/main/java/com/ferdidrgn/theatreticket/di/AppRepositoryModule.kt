package com.ferdidrgn.theatreticket.di

import com.ferdidrgn.theatreticket.repository.ForFirebaseQueries
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppRepositoryModule {

    @Singleton
    @Provides
    fun provideForFirebaseQueries() = ForFirebaseQueries()
}