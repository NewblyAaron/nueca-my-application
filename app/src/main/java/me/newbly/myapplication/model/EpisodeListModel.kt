package me.newbly.myapplication.model

import androidx.paging.Pager
import androidx.paging.PagingConfig
import me.newbly.myapplication.AnimeDetailsContract
import me.newbly.myapplication.api.JikanService
import me.newbly.myapplication.model.datamodel.EpisodeData
import me.newbly.myapplication.repository.EpisodePagingSource


class EpisodeListModel(
    private val service: JikanService
) : AnimeDetailsContract.Model {
    override fun getPager(animeId: Int): Pager<Int, EpisodeData> = Pager(
        config = PagingConfig(
            initialLoadSize = PAGE_SIZE,
            pageSize = PAGE_SIZE,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { EpisodePagingSource(service, animeId) }
    )

    companion object {
        const val PAGE_SIZE = 99
    }
}