package me.newbly.myapplication.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import me.newbly.myapplication.api.JikanService
import me.newbly.myapplication.model.AnimeData
import me.newbly.myapplication.model.EpisodeData
import javax.inject.Singleton

@Singleton
class JikanRepository @Inject constructor(private val service: JikanService) {
    fun getAnimeListResult(query: String): Flow<PagingData<AnimeData>> = Pager(
        config = PagingConfig(
            initialLoadSize = ANIME_PAGE_SIZE,
            pageSize = ANIME_PAGE_SIZE,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { AnimePagingSource(service, query) }
    ).flow


    fun getEpisodeList(animeId: Int): Flow<PagingData<EpisodeData>> = Pager(
        config = PagingConfig(
            initialLoadSize = EPISODE_PAGE_SIZE,
            pageSize = EPISODE_PAGE_SIZE,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { EpisodePagingSource(service, animeId) }
    ).flow

    companion object {
        const val ANIME_PAGE_SIZE = 25
        const val EPISODE_PAGE_SIZE = 99
    }
}

