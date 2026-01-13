package com.app.tmdbtask.di

import com.app.tmdbtask.data.local.AppDatabase
import com.app.tmdbtask.data.remote.TmdbService
import com.app.tmdbtask.data.repo.MoviesRepositoryImpl
import com.app.tmdbtask.domain.repository.MoviesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMoviesRepository(
        service: TmdbService, db: AppDatabase, @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): MoviesRepository =
        MoviesRepositoryImpl(
            service, db,
            ioDispatcher
        )
}