package me.newbly.myapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import me.newbly.myapplication.R
import me.newbly.myapplication.databinding.AnimeListLoadStateViewItemBinding

class AnimeListLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<AnimeListLoadStateAdapter.AnimeListLoadStateViewHolder>() {
    class AnimeListLoadStateViewHolder(
        private val binding: AnimeListLoadStateViewItemBinding,
        retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.loadStateRetryButton.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.loadStateErrorText.text = loadState.error.localizedMessage
            }

            binding.loadStateProgressBar.isVisible = loadState is LoadState.Loading
            binding.loadStateRetryButton.isVisible = loadState is LoadState.Error
            binding.loadStateErrorText.isVisible = loadState is LoadState.Error
        }

        companion object {
            fun create(parent: ViewGroup, retry: () -> Unit): AnimeListLoadStateViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.anime_list_load_state_view_item, parent, false)
                val binding = AnimeListLoadStateViewItemBinding.bind(view)

                return AnimeListLoadStateViewHolder(binding, retry)
            }
        }
    }

    override fun onBindViewHolder(holder: AnimeListLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): AnimeListLoadStateViewHolder {
        return AnimeListLoadStateViewHolder.create(parent, retry)
    }
}