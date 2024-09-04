package me.newbly.myapplication.api

import me.newbly.myapplication.model.JikanAnimeListDataModel
import me.newbly.myapplication.model.JikanEpisodeListDataModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JikanService {
    @GET("anime")
    suspend fun getAnimeList(
        @Query("page")
        pageIndex: Int,
        @Query("q")
        query: String,
        @Query("sfw")
        sfw: Boolean = true
    ) : Response<JikanAnimeListDataModel>

    @GET("anime/{id}/episodes")
    suspend fun getEpisodes(
        @Path("id")
        animeId: Int,
        @Query("page")
        pageIndex: Int
    ) : Response<JikanEpisodeListDataModel>

    companion object {
        private const val API_URL = "https://api.jikan.moe/v4/"

        fun create(): JikanService {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(JikanService::class.java)
        }
    }
}