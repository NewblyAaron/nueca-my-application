package me.newbly.myapplication.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import me.newbly.myapplication.databinding.AnimeItemBinding
import me.newbly.myapplication.models.AnimeData

interface AnimeListClickListener {
    fun onAnimeListItemClick(view: View, data: AnimeData)
}

class AnimeListAdapter(private val animeListClickListener: AnimeListClickListener) : RecyclerView.Adapter<AnimeListAdapter.ViewHolder>() {
    private var animeList = ArrayList<AnimeData>()

    @SuppressLint("NotifyDataSetChanged")
    fun setAnimeList(animeList: List<AnimeData>) {
        this.animeList = animeList as ArrayList<AnimeData>
        notifyDataSetChanged()
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

    override fun getItemCount(): Int = animeList.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.animeTitle.text = animeList[position].title
        Picasso.get().load(animeList[position].images.jpg.imageUrl).into(holder.binding.animeImage)

        holder.binding.root.setOnClickListener {
            animeListClickListener.onAnimeListItemClick(it, animeList[position])
        }
    }
}