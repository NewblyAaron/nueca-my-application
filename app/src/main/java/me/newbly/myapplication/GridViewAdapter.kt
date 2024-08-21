package me.newbly.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class GridViewAdapter(private val dataSet: List<JikanService.AnimeData>) : RecyclerView.Adapter<GridViewAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.imageView)
        val seriesName: TextView = view.findViewById(R.id.seriesTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gridview_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = dataSet.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.seriesName.text = dataSet[position].title
        Picasso.get().load(dataSet[position].images.jpg.large_image_url).into(holder.image)
    }
}