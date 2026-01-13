package com.app.tmdbtask.domain.repository

import androidx.paging.PagingData
import com.app.tmdbtask.data.local.MovieEntity
import com.app.tmdbtask.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    fun getTrendingMovies(): Flow<PagingData<Movie>>

    fun getNowPlayingMovies(): Flow<PagingData<Movie>>

    fun getBookmarkedMovies(): Flow<List<Movie>>

    suspend fun toggleBookmark(movie: Movie)

    suspend fun getMovieDetails(movieId: Long) : Movie

    fun searchMoviesStream(queryFlow: Flow<String>): Flow<List<Movie>>

}