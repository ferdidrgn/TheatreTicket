package com.ferdidrgn.theatreticket.di

import com.ferdidrgn.theatreticket.repository.*
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
    fun provideUserFirebaseQueries() = UserFirebaseQueries()

    @Singleton
    @Provides
    fun provideShowFirebaseQueries() = ShowFirebaseQuieries()

    @Singleton
    @Provides
    fun provideSellFirebaseQueries() = SellFirebaseQueries()

    @Singleton
    @Provides
    fun provideStageFirebaseQueries() = StageFirebaseQueries()

    @Singleton
    @Provides
    fun provideAppToolsFireBase() = AppToolsFireBaseQueries()

    @Singleton
    @Provides
    fun provideSeatFirebaseQueries() = SeatFirebaseQuieries()

    @Singleton
    @Provides
    fun provideSeatsFirebaseQueries() = SeatsFirebaseQuieries()

}