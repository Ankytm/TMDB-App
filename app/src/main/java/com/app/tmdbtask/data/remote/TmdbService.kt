package com.app.tmdbtask.data.remote

import com.app.tmdbtask.data.dto.MovieDetailsDto
import com.app.tmdbtask.data.dto.MoviePageDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbService {

    @GET("trending/movie/day")
    suspend fun getTrending(@Query("page") page: Int): MoviePageDto

    @GET("movie/now_playing")
    suspend fun getNowPlaying(@Query("page") page: Int): MoviePageDto

    @GET("movie/{id}")
    suspend fun getMovieDetails(@Path("id") id: Long): MovieDetailsDto

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): MoviePageDto



}