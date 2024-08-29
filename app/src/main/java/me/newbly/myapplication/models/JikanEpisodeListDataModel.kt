package me.newbly.myapplication.models
import com.google.gson.annotations.SerializedName

data class JikanEpisodeListDataModel(
    @SerializedName("pagination")
    val pagination: Pagination,
    @SerializedName("data")
    val `data`: List<EpisodeData>
) {
    data class Pagination(
        @SerializedName("last_visible_page")
        val lastVisiblePage: Int,
        @SerializedName("has_next_page")
        val hasNextPage: Boolean
    )
}

data class EpisodeData(
    @SerializedName("mal_id")
    val malId: Int,
    @SerializedName("url")
    val url: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("title_japanese")
    val titleJapanese: String,
    @SerializedName("title_romanji")
    val titleRomanji: String,
    @SerializedName("aired")
    val aired: String,
    @SerializedName("score")
    val score: Double,
    @SerializedName("filler")
    val filler: Boolean,
    @SerializedName("recap")
    val recap: Boolean,
    @SerializedName("forum_url")
    val forumUrl: String
)