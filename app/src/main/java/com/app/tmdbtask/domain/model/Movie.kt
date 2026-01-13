package com.app.tmdbtask.domain.model

data class Movie(
    val id: Long,
    val title: String,
    val overview: String,
    val posterUrl: String?,
    val backdropPath: String?,
    val releaseDate: String?,
    val voteAverage: Double,
    val isBookmarked: Boolean = false,
    val category: Category? = null
) {
    enum class Category { TRENDING, NOW_PLAYING }
}

