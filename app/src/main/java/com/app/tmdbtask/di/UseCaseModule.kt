package com.app.tmdbtask.di

import com.app.tmdbtask.domain.repository.MoviesRepository
import com.app.tmdbtask.domain.usecase.GetBookmarkedMoviesUseCase
import com.app.tmdbtask.domain.usecase.GetMoviesDetailsUseCase
import com.app.tmdbtask.domain.usecase.GetNowPlayingMoviesUseCase
import com.app.tmdbtask.domain.usecase.GetTrendingMoviesUseCase
import com.app.tmdbtask.domain.usecase.SearchUseCase
import com.app.tmdbtask.domain.usecase.ToggleBookmarkUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetTrending(repo: MoviesRepository) = GetTrendingMoviesUseCase(repo)
    @Provides
    fun provideGetNowPlaying(repo: MoviesRepository) = GetNowPlayingMoviesUseCase(repo)
    @Provides
    fun provideGetBookmarks(repo: MoviesRepository) = GetBookmarkedMoviesUseCase(repo)
    @Provides
    fun provideToggleBookmark(repo: MoviesRepository) = ToggleBookmarkUseCase(repo)

    @Provides
    fun provideGetMoviesDetails(repo: MoviesRepository) = GetMoviesDetailsUseCase(repo)

    @Provides
    fun provideSearchUseCase(repo: MoviesRepository) = SearchUseCase(repo)
}
