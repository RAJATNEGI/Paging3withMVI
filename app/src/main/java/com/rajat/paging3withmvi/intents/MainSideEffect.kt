package com.rajat.paging3withmvi.intents

import com.rajat.paging3withmvi.tmdb.model.Movie

sealed class MainSideEffect{
    data class ShowMovieName(val movieName:String): MainSideEffect()
    data class ShowError(val message:String): MainSideEffect()
    data class Navigate(val movie: Movie): MainSideEffect()
}
