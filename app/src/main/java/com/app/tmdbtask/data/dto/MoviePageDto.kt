package com.app.tmdbtask.data.dto

data class MoviePageDto(
    val page: Int,
    val results: List<MovieDto>,
    val total_pages: Int,
    val total_results: Int
)

data class MovieDto(
    val id: Long,
    val title: String?,
    val overview: String?,
    val poster_path: String?,
    val backdrop_path: String?,
    val release_date: String?,
    val vote_average: Double?,
)

data class MovieDetailsDto(
    val id: Long,
    val title: String?,
    val overview: String?,
    val genres: List<GenreDto>?,
    val runtime: Int?,
    val release_date: String?,
    val vote_average: Double?,
    val poster_path: String?
)

data class GenreDto(val id: Int, val name: String)