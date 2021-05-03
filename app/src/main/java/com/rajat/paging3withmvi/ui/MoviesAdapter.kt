package com.rajat.paging3withmvi.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rajat.paging3withmvi.databinding.MovieItemBinding
import com.rajat.paging3withmvi.tmdb.model.Movie

class MoviesAdapter(diffCallback: DiffUtil.ItemCallback<Movie>, private val context: Context) :
        PagingDataAdapter<Movie, MoviesAdapter.MoviesViewHolder>(diffCallback) {
    companion object {
        private const val TAG = "PostAdapter"
        const val MOVIE_ITEM = 1
        const val LOADING_FOOTER = 2
    }

    var recyclerViewItemEvent: RecyclerViewItemEvent = context as MainActivity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return MoviesViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount) {
            LOADING_FOOTER
        } else {
            MOVIE_ITEM
        }
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        val movie: Movie? = getItem(position)
        holder.bind(movie)

    }

    private fun imageUrlGenerator(imagePath: String): String {
        val initial = "https://image.tmdb.org/t/p/original"
        return "$initial$imagePath"
    }


    object UserComparator : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            // Id is unique.
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }


    inner class MoviesViewHolder(private val binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.movieImage.setOnLongClickListener {
                recyclerViewItemEvent.onLongClickMovieItem(getItem(bindingAdapterPosition)?.originalTitle.toString())
                true
            }
        }

        fun bind(movie: Movie?) {
            Glide.with(context)
                    .load(imageUrlGenerator(movie?.posterPath.toString()))
                    .centerCrop()
                    .into(binding.movieImage)
        }
    }

    interface RecyclerViewItemEvent {
        fun onLongClickMovieItem(movieName: String)
    }
}