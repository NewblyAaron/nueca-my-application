package me.newbly.myapplication.model

import androidx.paging.Pager
import androidx.paging.PagingConfig
import me.newbly.myapplication.AnimeListContract
import me.newbly.myapplication.api.JikanService
import me.newbly.myapplication.model.datamodel.AnimeData
import me.newbly.myapplication.repository.AnimePagingSource

class AnimeListModel(
    private val service: JikanService
) : AnimeListContract.Model {
    override fun getPager(query: String): Pager<Int, AnimeData> = Pager(
        config = PagingConfig(
            initialLoadSize = PAGE_SIZE,
            pageSize = PAGE_SIZE,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { AnimePagingSource(service, query) }
    )

    companion object {
        const val PAGE_SIZE = 25
    }
}