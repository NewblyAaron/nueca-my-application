package me.newbly.myapplication.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import me.newbly.myapplication.databinding.AnimeItemBinding
import me.newbly.myapplication.models.AnimeData

class AnimeListAdapter : RecyclerView.Adapter<AnimeListAdapter.ViewHolder>() {
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
        Picasso.get().load(animeList[position].images.jpg.large_image_url).into(holder.binding.animeImage)

        holder.binding.root.setOnClickListener {
//            Toast.makeText(it.context, animeList[position].title_japanese, Toast.LENGTH_SHORT).show()
            Log.d("ANIMELIST", "${animeList[position].title} (${animeList[position].title_japanese})")
        }
    }
}