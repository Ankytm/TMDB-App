package com.app.tmdbtask.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.app.tmdbtask.data.local.AppDatabase
import com.app.tmdbtask.data.local.MovieEntity
import com.app.tmdbtask.data.mapper.toEntity
import com.app.tmdbtask.data.remote.TmdbService

@OptIn(ExperimentalPagingApi::class)
class NowPlayingRemoteMediator(
    private val service: TmdbService,
    private val db: AppDatabase
) : RemoteMediator<Int, MovieEntity>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, MovieEntity>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val last = state.lastItemOrNull()
                val next = last?.page?.plus(1) ?: return MediatorResult.Success(true)
                next
            }
        }

        return try {
            val response = service.getNowPlaying(page)
            val entities = response.results.map { it.toEntity("NOW_PLAYING", response.page) }

            db.withTransaction {
                if (loadType == LoadType.REFRESH) db.movieDao().clearCategory("NOW_PLAYING")
                db.movieDao().upsertAll(entities)
            }

            MediatorResult.Success(endOfPaginationReached = response.page >= response.total_pages)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

}