package me.newbly.myapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.newbly.myapplication.adapters.AnimeListAdapter.ViewHolder
import me.newbly.myapplication.databinding.AnimeItemBinding
import me.newbly.myapplication.databinding.EpisodeItemBinding
import me.newbly.myapplication.models.EpisodeData

class AnimeEpisodeListAdapter : RecyclerView.Adapter<AnimeEpisodeListAdapter.ViewHolder>() {
    private val episodeList = ArrayList<EpisodeData>()

    fun addToEpisodeList(newData: List<EpisodeData>) {
        episodeList.addAll(newData)
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

    override fun getItemCount(): Int = episodeList.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.episodeNum.text = episodeList[position].malId.toString()
        holder.binding.episodeEngTitle.text = episodeList[position].title
        holder.binding.episodeRomajiTitle.text = episodeList[position].titleRomanji
        holder.binding.episodeJapTitle.text = episodeList[position].titleJapanese
    }
}