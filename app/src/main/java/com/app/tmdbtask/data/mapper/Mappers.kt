package com.app.tmdbtask.data.mapper

import com.app.tmdbtask.data.dto.MovieDetailsDto
import com.app.tmdbtask.data.dto.MovieDto
import com.app.tmdbtask.data.local.MovieEntity
import com.app.tmdbtask.domain.model.Movie

fun MovieDto.toEntity(category: String, page: Int): MovieEntity =
    MovieEntity(
        id = id,
        title = title ?: "",
        overview = overview ?: "",
        posterPath = poster_path,
        backdropPath = backdrop_path,
        releaseDate = release_date,
        voteAverage = vote_average ?: 0.0,
        isBookmarked = false,
        category = category,
        page = page
    )

fun MovieEntity.toDomain(): Movie =
    Movie(
        id = id,
        title = title,
        overview = overview,
        posterUrl = "https://image.tmdb.org/t/p/w500$posterPath",
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        isBookmarked = isBookmarked,
        category = when (category) {
            "TRENDING" -> Movie.Category.TRENDING
            "NOW_PLAYING" -> Movie.Category.NOW_PLAYING
            else -> null
        }
    )

fun MovieDetailsDto.toDomain(category: String): MovieEntity =
        MovieEntity(
            id = id,
            title = title ?: "",
            overview = overview ?: "",
            posterPath = poster_path,
            backdropPath = "",
            releaseDate = release_date,
            voteAverage = vote_average ?: 0.0,
            isBookmarked = false,
            category = category,
            page = -1
        )