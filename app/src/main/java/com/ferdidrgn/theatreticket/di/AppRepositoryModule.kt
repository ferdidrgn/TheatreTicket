package com.ferdidrgn.theatreticket.di

import com.ferdidrgn.theatreticket.repository.CustomerFirebaseQueries
import com.ferdidrgn.theatreticket.repository.SellFirebaseQueries
import com.ferdidrgn.theatreticket.repository.ShowFirebaseQuieries
import com.ferdidrgn.theatreticket.repository.StageFirebaseQueries
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
    fun provideCustomerFirebaseQueries() = CustomerFirebaseQueries()

    @Singleton
    @Provides
    fun provideShowFirebaseQueries() = ShowFirebaseQuieries()

    @Singleton
    @Provides
    fun provideSellFirebaseQueries() = SellFirebaseQueries()

    @Singleton
    @Provides
    fun provideStageFirebaseQueries() = StageFirebaseQueries()
}