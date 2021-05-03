package com.rajat.paging3withmvi.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.rajat.paging3withmvi.*
import com.rajat.paging3withmvi.ui.MoviesAdapter.Companion.MOVIE_ITEM
import com.rajat.paging3withmvi.databinding.ActivityMainBinding
import com.rajat.paging3withmvi.intents.MainSideEffect
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity(), MoviesAdapter.RecyclerViewItemEvent {

    private val viewModel: MainViewModel by viewModels()
    lateinit var adapter: MoviesAdapter

    companion object {
        private const val TAG = "MainActivity"

    }

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        init()

        /**
         * SideEffects are also Actions
         * but the thing make them different from normal actions is that they do not contribute
         * in the state of the Application and mostly they are the single time event
         * */

        //handle sideEffects
        lifecycleScope.launchWhenCreated {
            viewModel.sideEffect.collect {
                when (it) {
                    is MainSideEffect.Navigate -> navigate()
                    is MainSideEffect.ShowError -> showToast(it.message)
                    is MainSideEffect.ShowMovieName -> showToast(it.movieName)
                }
            }
        }

        //observe app state changes
        lifecycleScope.launchWhenStarted {
            viewModel.viewState.observe(this@MainActivity, {
                renderSearchState(it.mainState)
            })
        }

        lifecycleScope.launch {
            viewModel.flow.collect {
                adapter.submitData(it)
            }
        }

    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigate() {
    }

    private fun init() {
        val layoutManager = GridLayoutManager(this, 3)
        setGridLayoutSpanSize(manager = layoutManager)
        adapter = MoviesAdapter(MoviesAdapter.UserComparator, this)
        binding.movieRecyclerView.adapter = adapter.withLoadStateFooter(HeaderFooterAdapter{adapter.retry()})
        binding.movieRecyclerView.layoutManager = layoutManager
    }


    private fun renderSearchState(state: MainState) {

    }

    override fun onLongClickMovieItem(movieName: String) {
        viewModel.eventSideEffect(MainSideEffect.ShowMovieName(movieName))
    }

    private fun setGridLayoutSpanSize(manager:GridLayoutManager){
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                // If progress will be shown then span size will be 1 otherwise it will be 2
               val viewType =   adapter.getItemViewType(position)
                return if(viewType == MOVIE_ITEM) 1 else 3
            }
        }
    }
}