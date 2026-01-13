package com.app.tmdbtask.ui.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.app.tmdbtask.data.local.MovieEntity
import com.app.tmdbtask.domain.model.Movie
import com.app.tmdbtask.domain.usecase.GetBookmarkedMoviesUseCase
import com.app.tmdbtask.domain.usecase.GetMoviesDetailsUseCase
import com.app.tmdbtask.domain.usecase.GetNowPlayingMoviesUseCase
import com.app.tmdbtask.domain.usecase.GetTrendingMoviesUseCase
import com.app.tmdbtask.domain.usecase.ToggleBookmarkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    getTrendingMovies: GetTrendingMoviesUseCase,
    getNowPlayingMovies: GetNowPlayingMoviesUseCase,
    getBookmarkedMovies: GetBookmarkedMoviesUseCase,
    private val toggleBookmark: ToggleBookmarkUseCase,
    private val getMoviesDetailsUseCase: GetMoviesDetailsUseCase
) : ViewModel() {

    private val _movieDetails = MutableStateFlow<Movie?>(null)
    val movieDetails: StateFlow<Movie?> = _movieDetails

    val trending: Flow<PagingData<Movie>> =
        getTrendingMovies().cachedIn(viewModelScope)

    val nowPlaying: Flow<PagingData<Movie>> =
        getNowPlayingMovies().cachedIn(viewModelScope)

    val bookmarks: StateFlow<List<Movie>> =
        getBookmarkedMovies().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun onBookmarkClicked(movie: Movie) {
        viewModelScope.launch { toggleBookmark(movie = movie) }
    }

    fun loadMovieDetails(movieId: Long) {
        viewModelScope.launch {
            _movieDetails.value = null

            _movieDetails.value = getMoviesDetailsUseCase(movieId = movieId)
        }
    }
}