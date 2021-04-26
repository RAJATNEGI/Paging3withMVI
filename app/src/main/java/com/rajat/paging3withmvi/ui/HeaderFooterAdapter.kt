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

class HeaderFooterAdapter(  private val retry: () -> Unit): LoadStateAdapter<HeaderFooterAdapter.HeaderFooterViewHolder>() {

    class HeaderFooterViewHolder(view: View, retry: () -> Unit,) : RecyclerView.ViewHolder(view){
        val progressBar: ProgressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val retryButton: Button = view.findViewById<Button>(R.id.retryBtn)
        val errorMsg: TextView = view.findViewById<TextView>(R.id.errorTv)

        init {
            retryButton.setOnClickListener {
                retry()
            }
        }
    }

    override fun onBindViewHolder(holder: HeaderFooterViewHolder, loadState: LoadState) {
        holder.progressBar.isVisible = loadState is LoadState.Loading
        holder.retryButton.isVisible = loadState is LoadState.Error
        holder.errorMsg.isVisible = loadState is LoadState.Error

    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): HeaderFooterViewHolder {
        return HeaderFooterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.loading_item,parent,false),retry)
    }
}