package com.ferdidrgn.theatreticket.di

import com.ferdidrgn.theatreticket.data.repository.AppToolsFireBaseQueriesRepository
import com.ferdidrgn.theatreticket.data.repository.AppToolsFireBaseQueriesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppRepositoryModule {
    @Binds
    @ViewModelScoped
    abstract fun bindAppToolsFireBaseQueriesRepository(appToolsFireBaseQueriesRepositoryImpl: AppToolsFireBaseQueriesRepositoryImpl): AppToolsFireBaseQueriesRepository

}