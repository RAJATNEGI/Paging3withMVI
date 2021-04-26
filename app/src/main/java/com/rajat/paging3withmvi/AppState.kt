package com.rajat.paging3withmvi

import com.rajat.paging3withmvi.tmdb.model.Movie


data class AppState(
    val mainState: MainState
)

 data class MainState(
     val results: List<Movie>? = emptyList(),
     val isLoading:Boolean = true
)

