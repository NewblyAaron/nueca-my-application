package me.newbly.myapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import me.newbly.myapplication.AnimeListContract
import me.newbly.myapplication.databinding.AnimeItemBinding
import me.newbly.myapplication.model.datamodel.AnimeData
import me.newbly.myapplication.ui.animelist.AnimeListPresenter

class AnimeListAdapter(
    private val presenter: AnimeListContract.Presenter
) :
    PagingDataAdapter<AnimeData, AnimeListAdapter.ViewHolder>(AnimeDataDiffCallBack()) {
    class AnimeDataDiffCallBack : DiffUtil.ItemCallback<AnimeData>()  {
        override fun areItemsTheSame(oldItem: AnimeData, newItem: AnimeData): Boolean =
            oldItem.malId == newItem.malId

        override fun areContentsTheSame(oldItem: AnimeData, newItem: AnimeData): Boolean =
            oldItem == newItem
    }

    class ViewHolder(
        private val binding: AnimeItemBinding) :
        RecyclerView.ViewHolder(binding.root), AnimeListContract.ItemView {

        override fun bindItem(animeData: AnimeData?) {
            binding.animeTitle.text =
                animeData?.titleEnglish ?: animeData?.title
            Picasso.get().load(animeData?.images?.jpg?.imageUrl).into(binding.animeImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            binding = AnimeItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repoItem: AnimeData? = getItem(position)
        holder.bindItem(repoItem)
        holder.itemView.setOnClickListener { presenter.onListItemClick(repoItem) }
    }
}