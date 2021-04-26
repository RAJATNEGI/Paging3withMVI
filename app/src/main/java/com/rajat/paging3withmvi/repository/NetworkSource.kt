package com.rajat.paging3withmvi.repository

import com.rajat.paging3withmvi.tmdb.model.PopularMoviesResponse


interface NetworkSource {
    suspend fun getPopularMovies(pageNumber: Int): Resource<PopularMoviesResponse?>
}