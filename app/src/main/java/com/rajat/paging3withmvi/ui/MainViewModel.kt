package com.rajat.paging3withmvi.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.rajat.paging3withmvi.AppState
import com.rajat.paging3withmvi.MainActions
import com.rajat.paging3withmvi.MainState
import com.rajat.paging3withmvi.Reducible
import com.rajat.paging3withmvi.intents.MainSideEffect
import com.rajat.paging3withmvi.network.RetrofitClient
import com.rajat.paging3withmvi.network.TmdbApiService
import com.rajat.paging3withmvi.paging.MyDataSource
import com.rajat.paging3withmvi.repository.NetworkSourceRepository
import com.rajat.paging3withmvi.tmdb.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {


    companion object {
        private const val TAG = "MainViewModel"
        private const val MAXIMUM_LIMIT_OF_CACHE_ITEMS = 200
    }

    private val _viewState = MutableLiveData<AppState>()
    private val _sideEffects = Channel<MainSideEffect>(Channel.BUFFERED)

    val sideEffect: Flow<MainSideEffect> = _sideEffects.receiveAsFlow()
    val viewState: LiveData<AppState>
        get() = _viewState

    private var repository: NetworkSourceRepository =
        NetworkSourceRepository(RetrofitClient.getService(TmdbApiService::class.java))

    val flow = Pager<Int, Movie>(
        PagingConfig(
            pageSize = 10,
            prefetchDistance = 1,
            enablePlaceholders = true,
            initialLoadSize = 10,
            maxSize = MAXIMUM_LIMIT_OF_CACHE_ITEMS
        )
    ) {
        MyDataSource(repository)
    }.flow.cachedIn(viewModelScope)


    init {
        val initialState = AppState(MainState(isLoading = true))
        _viewState.value = initialState
        eventMain(MainActions.FetchPopularMovies)
    }

    fun eventMain(action: MainActions) {
        val currentPostState = _viewState.value!!.mainState
        when (action) {
            is MainActions.LoadMovies -> {
                _viewState.value = _viewState.value?.copy(mainState = reduce(currentPostState, action))
            }

            is MainActions.FetchPopularMovies -> {
                _viewState.value = _viewState.value?.copy(mainState = reduce(currentPostState, action))
            }

            is MainActions.IsLoading -> {
                _viewState.value = _viewState.value?.copy(
                    mainState = reduce(
                        _viewState.value!!.mainState,
                        MainActions.IsLoading
                    )
                )
            }

            is MainActions.NotLoading -> {
                _viewState.value = _viewState.value?.copy(
                    mainState = reduce(
                        _viewState.value!!.mainState,
                        MainActions.NotLoading
                    )
                )
            }
            else -> println("nothing changes")
        }
    }

    fun eventSideEffect(action: MainSideEffect) {
        when (action) {
            is MainSideEffect.ShowMovieName -> {
                //Trigger sideEffect
                viewModelScope.launch(Dispatchers.Main) {
                    _sideEffects.send(MainSideEffect.ShowMovieName(action.movieName))
                }
            }
            is MainSideEffect.ShowError -> { //Trigger sideEffect
                viewModelScope.launch(Dispatchers.Main) {
                    _sideEffects.send(MainSideEffect.ShowError(action.message))
                }
            }
            is MainSideEffect.Navigate -> {
            }
        }
    }


    private fun reduce(oldMainState: MainState, action: MainActions): MainState {
        return when (action) {
            is Reducible -> {
                action.reduce(oldState = oldMainState, action = action)
            }
            else -> {
                oldMainState
            }
        }
    }

    private fun getUsers() {
//        viewModelScope.launch {
//            val value = repository.getPopularMovies()
//            when (value) {
//                is Resource.Error -> {
//                    eventSideEffect(MainSideEffect.ShowError(value.message.toString()))
//                }
//                is Resource.Loading -> {
//                    eventMain(MainActions.IsLoading)
//                }
//                is Resource.Success -> {
//                    eventMain(MainActions.LoadUsers(value.data))
//                }
//            }
//
//        }
    }


    override fun onCleared() {
        super.onCleared()
    }
}