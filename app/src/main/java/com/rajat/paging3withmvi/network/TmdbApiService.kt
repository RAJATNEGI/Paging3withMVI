package com.rajat.paging3withmvi.network

import com.rajat.paging3withmvi.tmdb.model.PopularMoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApiService {
    @GET("3/movie/popular")
    suspend fun getPopularMovies(@Query("page")pageNumber:Int): PopularMoviesResponse?
}