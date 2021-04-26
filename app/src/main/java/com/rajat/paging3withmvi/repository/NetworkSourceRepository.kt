package com.rajat.paging3withmvi.repository

import com.rajat.paging3withmvi.network.TmdbApiService
import com.rajat.paging3withmvi.tmdb.model.PopularMoviesResponse
import java.io.IOException

class NetworkSourceRepository(private val apiService: TmdbApiService) : NetworkSource {
    override suspend fun getPopularMovies(pageNumber:Int): Resource<PopularMoviesResponse?> {
        return try {
            val result = apiService.getPopularMovies(pageNumber)
            Resource.Success(result)
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(null, e.message.toString())
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(null, e.message.toString())
        }
    }


}