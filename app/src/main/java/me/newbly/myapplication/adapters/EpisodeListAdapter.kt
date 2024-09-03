package me.newbly.myapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import me.newbly.myapplication.databinding.EpisodeItemBinding
import me.newbly.myapplication.model.EpisodeData

class EpisodeListAdapter : PagingDataAdapter<EpisodeData, EpisodeListAdapter.ViewHolder>(
    EpisodeDataDiffCallBack()
)  {
    class EpisodeDataDiffCallBack : DiffUtil.ItemCallback<EpisodeData>()  {
        override fun areItemsTheSame(oldItem: EpisodeData, newItem: EpisodeData): Boolean =
            oldItem.malId == newItem.malId

        override fun areContentsTheSame(oldItem: EpisodeData, newItem: EpisodeData): Boolean =
            oldItem == newItem
    }

    class ViewHolder(val binding: EpisodeItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            EpisodeItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repoItem = getItem(position)

        holder.binding.episodeNum.text = repoItem?.malId.toString()
        holder.binding.episodeEngTitle.text = repoItem?.title
        holder.binding.episodeRomajiTitle.text = repoItem?.titleRomanji
        holder.binding.episodeJapTitle.text = repoItem?.titleJapanese
    }
}