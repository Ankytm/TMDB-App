package com.app.tmdbtask.domain.usecase

import androidx.paging.PagingData
import com.app.tmdbtask.domain.model.Movie
import com.app.tmdbtask.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetTrendingMoviesUseCase @Inject constructor(
    private val repo: MoviesRepository) {
    operator fun invoke(): Flow<PagingData<Movie>> = repo.getTrendingMovies()
}

class GetNowPlayingMoviesUseCase(private val repo: MoviesRepository) {
    operator fun invoke(): Flow<PagingData<Movie>> = repo.getNowPlayingMovies()
}

class GetBookmarkedMoviesUseCase(private val repo: MoviesRepository) {
    operator fun invoke(): Flow<List<Movie>> = repo.getBookmarkedMovies()
}

class ToggleBookmarkUseCase(private val repo: MoviesRepository) {
    suspend operator fun invoke(movie: Movie) = repo.toggleBookmark(movie = movie)
}

class GetMoviesDetailsUseCase(private val repo: MoviesRepository){
    suspend operator fun invoke(movieId: Long) = repo.getMovieDetails(movieId = movieId)
}

class SearchUseCase(private val repo: MoviesRepository) {
    operator fun invoke(queryFlow: Flow<String>) = repo.searchMoviesStream(queryFlow= queryFlow)
}
