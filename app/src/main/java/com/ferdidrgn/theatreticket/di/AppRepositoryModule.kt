package com.ferdidrgn.theatreticket.di

import com.ferdidrgn.theatreticket.domain.repository.AppToolsFireBaseQueriesRepository
import com.ferdidrgn.theatreticket.data.repository.AppToolsFireBaseQueriesRepositoryImpl
import com.ferdidrgn.theatreticket.domain.repository.SeatFirebaseQueriesRepository
import com.ferdidrgn.theatreticket.data.repository.SeatFirebaseQueriesRepositoryImpl
import com.ferdidrgn.theatreticket.data.repository.SeatsFirebaseQuieries
import com.ferdidrgn.theatreticket.data.repository.SellFirebaseQueries
import com.ferdidrgn.theatreticket.data.repository.ShowFirebaseQuieries
import com.ferdidrgn.theatreticket.data.repository.StageFirebaseQueries
import com.ferdidrgn.theatreticket.data.repository.UserFirebaseQueries
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppRepositoryModule {
    @Binds
    @ViewModelScoped
    abstract fun bindAppToolsFireBaseQueriesRepository(appToolsFireBaseQueriesRepositoryImpl: AppToolsFireBaseQueriesRepositoryImpl): AppToolsFireBaseQueriesRepository

    @Binds
    @ViewModelScoped
    abstract fun bindSeatFirebaseQueriesRepository(seatFirebaseQueriesRepository: SeatFirebaseQueriesRepositoryImpl): SeatFirebaseQueriesRepository

}