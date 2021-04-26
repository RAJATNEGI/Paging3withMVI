package com.rajat.paging3withmvi.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bumptech.glide.load.HttpException
import com.rajat.paging3withmvi.repository.NetworkSourceRepository
import com.rajat.paging3withmvi.repository.Resource
import com.rajat.paging3withmvi.tmdb.model.Movie
import com.rajat.paging3withmvi.tmdb.model.PopularMoviesResponse
import java.io.IOException

class MyDataSource(private val repository: NetworkSourceRepository) : PagingSource<Int, Movie>() {
    companion object{
        const val STARTING_PAGE_INDEX = 1
    }
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val nextPageNumber = params.key ?: STARTING_PAGE_INDEX
            val response:Resource<PopularMoviesResponse?> = repository.getPopularMovies(nextPageNumber)
            when(response){
                is Resource.Error -> { throw Exception(response.message)}
                is Resource.Success -> {
                    val data: List<Movie>? = response.data?.results
                    val totalPages = response.data?.totalPages
                    LoadResult.Page(
                        prevKey =  if(nextPageNumber< (STARTING_PAGE_INDEX+1)) null else nextPageNumber.minus(1) ,
                        nextKey = if(nextPageNumber<totalPages!!) nextPageNumber.plus(1) else null,
                        data = data!!
                    )
                }
            }

        } catch (e: IOException) {
            // IOException for network failures.
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            // HttpException for any non-2xx HTTP status codes.
            return LoadResult.Error(e)
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

}