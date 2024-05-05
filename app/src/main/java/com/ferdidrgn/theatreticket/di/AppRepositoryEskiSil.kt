package com.ferdidrgn.theatreticket.di

import com.ferdidrgn.theatreticket.data.repository.SeatsFirebaseQuieries
import com.ferdidrgn.theatreticket.data.repository.SellFirebaseQueries
import com.ferdidrgn.theatreticket.data.repository.ShowFirebaseQuieries
import com.ferdidrgn.theatreticket.data.repository.StageFirebaseQueries
import com.ferdidrgn.theatreticket.data.repository.UserFirebaseQueries
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppRepositoryEskiSil {

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
    fun provideSeatsFirebaseQueries() = SeatsFirebaseQuieries()
}