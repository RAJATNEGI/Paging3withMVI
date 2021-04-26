package com.rajat.paging3withmvi

import com.rajat.paging3withmvi.tmdb.model.Movie


sealed class MainActions {
    object FetchPopularMovies : MainActions(), Reducible {
        override fun reduce(oldState: MainState, action: MainActions): MainState {
            return oldState.copy(
                isLoading = true
            )
        }
    }

    data class LoadMovies(val movies: List<Movie>?) : MainActions(), Reducible {
        override fun reduce(oldState: MainState, action: MainActions): MainState {
            return oldState.copy(
                isLoading = false,
                results = movies
            )
        }
    }

    object Refresh : MainActions(), Reducible {
        override fun reduce(oldState: MainState, action: MainActions): MainState {
            return oldState.copy(
                isLoading = false
            )
        }
    }

    object IsLoading : MainActions(), Reducible {
        override fun reduce(oldState: MainState, action: MainActions): MainState {
            return oldState.copy(
                isLoading = true
            )
        }
    }

    object NotLoading : MainActions(), Reducible {
        override fun reduce(oldState: MainState, action: MainActions): MainState {
            return oldState.copy(
                isLoading = false
            )
        }

    }

}

interface Reducible {
    fun reduce(oldState: MainState, action: MainActions): MainState
}