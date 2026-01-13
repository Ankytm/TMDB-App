package com.app.tmdbtask.data.repo

import androidx.paging.*
import com.app.tmdbtask.data.local.AppDatabase
import com.app.tmdbtask.data.local.MovieEntity
import com.app.tmdbtask.data.mapper.toDomain
import com.app.tmdbtask.data.mapper.toEntity
import com.app.tmdbtask.data.mediator.NowPlayingRemoteMediator
import com.app.tmdbtask.data.mediator.TrendingRemoteMediator
import com.app.tmdbtask.data.remote.TmdbService
import com.app.tmdbtask.di.IoDispatcher
import com.app.tmdbtask.domain.model.Movie
import com.app.tmdbtask.domain.repository.MoviesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val api: TmdbService,
    private val db: AppDatabase,
    @IoDispatcher private val io: CoroutineDispatcher
) : MoviesRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getTrendingMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            remoteMediator = TrendingRemoteMediator(api, db),
            pagingSourceFactory = { db.movieDao().moviesByCategory("TRENDING") }
        ).flow.map { paging -> paging.map(MovieEntity::toDomain) }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getNowPlayingMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            remoteMediator = NowPlayingRemoteMediator(api, db),
            pagingSourceFactory = { db.movieDao().moviesByCategory("NOW_PLAYING") }
        ).flow.map { paging -> paging.map(MovieEntity::toDomain) }
    }

    override fun getBookmarkedMovies(): Flow<List<Movie>> {
        return db.movieDao().bookmarkedMovies().map { list -> list.map(MovieEntity::toDomain) }
    }

    override suspend fun toggleBookmark(movie: Movie) {
        db.movieDao().toggleBookmark(movie.id)
    }

    override suspend fun getMovieDetails(movieId: Long): Movie {
        // 1️⃣ Try local DB first (from trending/now playing cache)
        val cached = db.movieDao().getMovieById(movieId)
        if (cached != null) return cached.toDomain()

        // 2️⃣ If not found, fetch from remote
        val response = api.getMovieDetails(movieId)
        val entity = response.toDomain("details")

        // 3️⃣ Cache it locally
        db.movieDao().upsert(entity)

        return entity.toDomain()
    }


    private val dao = db.movieDao()
    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    override fun searchMoviesStream(queryFlow: Flow<String>): Flow<List<Movie>> =
        queryFlow
            .map { it.trim() }
            .debounce(400)
            .distinctUntilChanged()
            .flatMapLatest { query ->
                if (query.isBlank()) {
                    flowOf(emptyList())
                } else {
                    // 1) emit local results immediately
                    val local = dao.searchLocal(query).map { list -> list.map(MovieEntity::toDomain)  }

                    // 2) refresh remote in background and cache
                    flow {
                        emitAll(local) // immediate local
                        withContext(io) {
                            try {
                                val response = api.searchMovies(query = query, page = 1)
                                val entities = response.results.map { it.toEntity(category = "search", page = response.page) }
                                dao.upsertAll(entities)
                            } catch (_: Exception) {
                                // swallow errors; UI keeps local results
                            }
                        }
                        emitAll(local) // re-emit after cache update
                    }
                }
            }
            .flowOn(io)



}
