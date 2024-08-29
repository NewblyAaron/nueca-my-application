package me.newbly.myapplication.repositories

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.newbly.myapplication.models.AnimeData
import me.newbly.myapplication.models.EpisodeData
import me.newbly.myapplication.models.JikanAnimeListDataModel
import me.newbly.myapplication.models.JikanEpisodeListDataModel
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private interface JikanApi {
    @GET("anime")
    suspend fun getAnimeList(
        @Query("page")
        pageIndex: Int
    ) : Response<JikanAnimeListDataModel>

    @GET("anime/{id}/episodes")
    suspend fun getEpisodes(
        @Path("id")
        animeId: Int,
        @Query("page")
        pageIndex: Int
    ) : Response<JikanEpisodeListDataModel>
}

private class JikanDataSource {
    private object RetrofitInstance {
        val api: JikanApi by lazy {
            Retrofit.Builder()
                .baseUrl("https://api.jikan.moe/v4/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(JikanApi::class.java)
        }
    }

    fun getAnimeList(pageIndex: Int): Flow<List<AnimeData>> = flow {
        val animeList = RetrofitInstance.api.getAnimeList(pageIndex)

        if (!animeList.isSuccessful) {
            throw HttpException(animeList)
        }

        emit(animeList.body()!!.data)
    }

    fun getEpisodeList(animeId: Int): Flow<List<EpisodeData>> = flow {
        var pageIndex = 1
        val episodeList: MutableList<EpisodeData> = mutableListOf()
        do {
            val response = RetrofitInstance.api.getEpisodes(animeId, pageIndex)

            if (!response.isSuccessful) {
                throw HttpException(response)
            }

            Log.d("JikanDataSource", "fetched episodes in page $pageIndex")
            episodeList.addAll(response.body()!!.data)
            pageIndex++
        } while (response.body()!!.pagination.hasNextPage)

        emit(episodeList)
    }
}

class JikanRepository {
    private val dataSource = JikanDataSource()

    fun getAnimeList(pageIndex: Int): Flow<List<AnimeData>> = dataSource.getAnimeList(pageIndex)
    fun getEpisodeList(animeId: Int): Flow<List<EpisodeData>> = dataSource.getEpisodeList(animeId)
}

