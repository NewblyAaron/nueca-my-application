package me.newbly.myapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import me.newbly.myapplication.databinding.AnimeItemBinding
import me.newbly.myapplication.model.AnimeData

class AnimeListAdapter(private val animeListClickListener: AnimeListClickListener) :
    PagingDataAdapter<AnimeData, AnimeListAdapter.ViewHolder>(AnimeDataDiffCallBack()) {
    class AnimeDataDiffCallBack : DiffUtil.ItemCallback<AnimeData>()  {
        override fun areItemsTheSame(oldItem: AnimeData, newItem: AnimeData): Boolean =
            oldItem.malId == newItem.malId

        override fun areContentsTheSame(oldItem: AnimeData, newItem: AnimeData): Boolean =
            oldItem == newItem
    }

    class ViewHolder(val binding: AnimeItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AnimeItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repoItem = getItem(position)
        holder.binding.animeTitle.text =
            repoItem?.titleEnglish ?: repoItem?.title
        Picasso.get().load(repoItem?.images?.jpg?.imageUrl).into(holder.binding.animeImage)

        holder.binding.root.setOnClickListener {
            animeListClickListener.onAnimeListItemClick(it, repoItem!!)
        }
    }
}

interface AnimeListClickListener {
    fun onAnimeListItemClick(view: View, data: AnimeData)
}