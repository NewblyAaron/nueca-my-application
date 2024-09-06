package me.newbly.myapplication

import androidx.paging.Pager
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import me.newbly.myapplication.model.datamodel.AnimeData
import me.newbly.myapplication.model.datamodel.EpisodeData
import me.newbly.myapplication.ui.animedetails.AnimeDetailsPresenter

interface AnimeListContract {
    interface View {
        fun submitPagingData(pagingDataFlow: Flow<PagingData<AnimeData>>)
        fun retryFetchingData()
        fun showAnimeDetails(animeData: AnimeData)
    }

    interface ItemView {
        fun bindItem(animeData: AnimeData?)
    }

    interface Presenter {
        fun onListItemClick(animeData: AnimeData?)
        fun onRetryButtonClick()
        fun onQueryTextSubmit(query: String?): Boolean
        fun onQueryTextChange(newText: String?): Boolean

        fun loadPagingData(searchQuery: String = "")
    }

    interface Model {
        fun getPager(query: String) : Pager<Int, AnimeData>
    }
}

interface AnimeDetailsContract {
    interface View {
        fun submitPagingData(pagingDataFlow: Flow<PagingData<EpisodeData>>)
    }

    interface Presenter {
        fun loadPagingData(animeId: Int)
    }

    interface Model {
        fun getPager(animeId: Int): Pager<Int, EpisodeData>
    }
}