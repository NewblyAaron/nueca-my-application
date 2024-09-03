package me.newbly.myapplication.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class JikanAnimeListDataModel(
    @SerializedName("pagination")
    val pagination: Pagination,
    @SerializedName("data")
    val `data`: List<AnimeData>
) : Serializable {
    data class Pagination(
        @SerializedName("last_visible_page")
        val lastVisiblePage: Int,
        @SerializedName("has_next_page")
        val hasNextPage: Boolean,
        @SerializedName("current_page")
        val currentPage: Int,
        @SerializedName("items")
        val items: Items
    ): Serializable
}

data class AnimeData(
    @SerializedName("mal_id")
    val malId: Int,
    @SerializedName("url")
    val url: String,
    @SerializedName("images")
    val images: Images,
    @SerializedName("trailer")
    val trailer: Trailer,
    @SerializedName("approved")
    val approved: Boolean,
    @SerializedName("titles")
    val titles: List<Title>,
    @SerializedName("title")
    val title: String,
    @SerializedName("title_english")
    val titleEnglish: String?,
    @SerializedName("title_japanese")
    val titleJapanese: String,
    @SerializedName("title_synonyms")
    val titleSynonyms: List<String>,
    @SerializedName("type")
    val type: String,
    @SerializedName("source")
    val source: String,
    @SerializedName("episodes")
    val episodes: Int?,
    @SerializedName("status")
    val status: String,
    @SerializedName("airing")
    val airing: Boolean,
    @SerializedName("aired")
    val aired: Aired,
    @SerializedName("duration")
    val duration: String,
    @SerializedName("rating")
    val rating: String,
    @SerializedName("score")
    val score: Double,
    @SerializedName("scored_by")
    val scoredBy: Int,
    @SerializedName("rank")
    val rank: Int,
    @SerializedName("popularity")
    val popularity: Int,
    @SerializedName("members")
    val members: Int,
    @SerializedName("favorites")
    val favorites: Int,
    @SerializedName("synopsis")
    val synopsis: String,
    @SerializedName("background")
    val background: String,
    @SerializedName("season")
    val season: String?,
    @SerializedName("year")
    val year: Int?,
    @SerializedName("broadcast")
    val broadcast: Broadcast,
    @SerializedName("producers")
    val producers: List<Producer>,
    @SerializedName("licensors")
    val licensors: List<Licensor>,
    @SerializedName("studios")
    val studios: List<Studio>,
    @SerializedName("genres")
    val genres: List<Genre>,
    @SerializedName("explicit_genres")
    val explicitGenres: List<Any>,
    @SerializedName("themes")
    val themes: List<Theme>,
    @SerializedName("demographics")
    val demographics: List<Demographic>
): Serializable

data class Items(
    @SerializedName("count")
    val count: Int,
    @SerializedName("total")
    val total: Int,
    @SerializedName("per_page")
    val perPage: Int
): Serializable

data class Images(
    @SerializedName("jpg")
    val jpg: Jpg,
    @SerializedName("webp")
    val webp: Webp
): Serializable

data class Trailer(
    @SerializedName("youtube_id")
    val youtubeId: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("embed_url")
    val embedUrl: String?,
    @SerializedName("images")
    val images: ImagesX
): Serializable

data class Title(
    @SerializedName("type")
    val type: String,
    @SerializedName("title")
    val title: String
): Serializable

data class Aired(
    @SerializedName("from")
    val from: String,
    @SerializedName("to")
    val to: String?,
    @SerializedName("prop")
    val prop: Prop,
    @SerializedName("string")
    val string: String
): Serializable

data class Broadcast(
    @SerializedName("day")
    val day: String?,
    @SerializedName("time")
    val time: String?,
    @SerializedName("timezone")
    val timezone: String?,
    @SerializedName("string")
    val string: String?
): Serializable

data class Producer(
    @SerializedName("mal_id")
    val malId: Int,
    @SerializedName("type")
    val type: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
): Serializable

data class Licensor(
    @SerializedName("mal_id")
    val malId: Int,
    @SerializedName("type")
    val type: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
): Serializable

data class Studio(
    @SerializedName("mal_id")
    val malId: Int,
    @SerializedName("type")
    val type: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
): Serializable

data class Genre(
    @SerializedName("mal_id")
    val malId: Int,
    @SerializedName("type")
    val type: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
): Serializable

data class Theme(
    @SerializedName("mal_id")
    val malId: Int,
    @SerializedName("type")
    val type: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
): Serializable

data class Demographic(
    @SerializedName("mal_id")
    val malId: Int,
    @SerializedName("type")
    val type: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
): Serializable

data class Jpg(
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("small_image_url")
    val smallImageUrl: String,
    @SerializedName("large_image_url")
    val largeImageUrl: String
): Serializable

data class Webp(
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("small_image_url")
    val smallImageUrl: String,
    @SerializedName("large_image_url")
    val largeImageUrl: String
): Serializable

data class ImagesX(
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("small_image_url")
    val smallImageUrl: String?,
    @SerializedName("medium_image_url")
    val mediumImageUrl: String?,
    @SerializedName("large_image_url")
    val largeImageUrl: String?,
    @SerializedName("maximum_image_url")
    val maximumImageUrl: String?
): Serializable

data class Prop(
    @SerializedName("from")
    val from: From,
    @SerializedName("to")
    val to: To
): Serializable

data class From(
    @SerializedName("day")
    val day: Int,
    @SerializedName("month")
    val month: Int,
    @SerializedName("year")
    val year: Int
): Serializable

data class To(
    @SerializedName("day")
    val day: Int?,
    @SerializedName("month")
    val month: Int?,
    @SerializedName("year")
    val year: Int?
): Serializable