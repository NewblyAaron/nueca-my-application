package me.newbly.myapplication.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.newbly.myapplication.models.AnimeData
import me.newbly.myapplication.models.JikanDataModel
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private interface JikanApi {
    @GET("anime")
    suspend fun getAnimeList(
        @Query("page")
        pageIndex: Int
    ) : Response<JikanDataModel>
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
}

class JikanRepository {
    private val dataSource = JikanDataSource()

    fun getAnimeList(pageIndex: Int): Flow<List<AnimeData>> = dataSource.getAnimeList(pageIndex)
}

