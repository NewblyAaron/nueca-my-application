package me.newbly.myapplication

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.io.Serializable

class JikanService {
    data class Anime(
        val pagination: PaginationData,
        val data: List<AnimeData>
    )

    data class PaginationData(
        val last_visible_page: Int,
        val has_next_page: Boolean,
        val current_page: Int,
        val items: Map<String, Int>
    )

    data class AnimeData(
        val mal_id: Int,
        val title: String,
        val title_japanese: String,
        val images: AnimeImageType
    )

    data class AnimeImageType(
        val jpg: AnimeImageJpegUrls,
    )

    data class AnimeImageJpegUrls(
        val image_url: String,
        val small_image_url: String,
        val large_image_url: String
    )

    interface Jikan {
        @GET("anime")
        fun getAnimes() : Call<Anime>
    }
}