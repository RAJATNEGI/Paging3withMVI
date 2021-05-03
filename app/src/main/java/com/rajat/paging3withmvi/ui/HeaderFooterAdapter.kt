package com.rajat.paging3withmvi.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rajat.paging3withmvi.R
import com.rajat.paging3withmvi.databinding.ActivityMainBinding
import com.rajat.paging3withmvi.databinding.LoadingItemBinding

class HeaderFooterAdapter(private val retry: () -> Unit): LoadStateAdapter<HeaderFooterAdapter.HeaderFooterViewHolder>() {

   class HeaderFooterViewHolder(private val binding: LoadingItemBinding,retry: () -> Unit,) : RecyclerView.ViewHolder(binding.root){

        init {
            binding.retryBtn.setOnClickListener {
                retry()
            }
        }

      fun bind(loadState: LoadState){
          binding.progressBar.isVisible = loadState is LoadState.Loading
          binding.retryBtn.isVisible = loadState is LoadState.Error
          binding.errorTv.isVisible = loadState is LoadState.Error
      }
    }

    override fun onBindViewHolder(holder: HeaderFooterViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): HeaderFooterViewHolder {
        val binding = LoadingItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return HeaderFooterViewHolder(binding,retry)
    }


}