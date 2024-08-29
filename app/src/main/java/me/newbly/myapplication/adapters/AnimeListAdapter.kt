package me.newbly.myapplication.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import me.newbly.myapplication.databinding.AnimeItemBinding
import me.newbly.myapplication.models.AnimeData

interface AnimeListClickListener {
    fun onAnimeListItemClick(view: View, data: AnimeData)
}

class AnimeListAdapter(private val animeListClickListener: AnimeListClickListener) : RecyclerView.Adapter<AnimeListAdapter.ViewHolder>() {
    private var animeList = ArrayList<AnimeData>()
    private var _loadedPages = 0
    val loadedPages
        get() = _loadedPages

    fun setAnimeList(animeList: List<AnimeData>) {
        this.animeList = animeList as ArrayList<AnimeData>
    }

    fun addToAnimeList(newData: List<AnimeData>) {
        animeList.addAll(newData)
        _loadedPages++
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
        holder.binding.animeTitle.text = animeList[position].titleEnglish ?: animeList[position].title
        Picasso.get().load(animeList[position].images.jpg.imageUrl).into(holder.binding.animeImage)

        holder.binding.root.setOnClickListener {
            animeListClickListener.onAnimeListItemClick(it, animeList[position])
        }
    }
}

abstract class PaginationScrollListener : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val layoutManager = recyclerView.layoutManager as GridLayoutManager
        val visibleItemCount = layoutManager.findLastVisibleItemPosition()
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if (!isLoading()) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                Log.d("PaginationScrollListener", "loading more items")
                loadMoreItems()
            }
        }
    }

    abstract fun isLoading(): Boolean
    abstract fun loadMoreItems()
}