package me.newbly.myapplication.ui.animelist

import me.newbly.myapplication.AnimeListContract
import me.newbly.myapplication.model.datamodel.AnimeData

class AnimeListPresenter(
    private val view: AnimeListContract.View,
    private val model: AnimeListContract.Model
) : AnimeListContract.Presenter {
    override fun onRetryButtonClick() {
        view.retryFetchingData()
    }

    override fun loadPagingData(searchQuery: String) {
        val pager = model.getPager(searchQuery)
        val pagingData = pager.flow

        view.submitPagingData(pagingData)
    }

    override fun onListItemClick(animeData: AnimeData?) {
        if (animeData != null) {
            view.showAnimeDetails(animeData)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        loadPagingData(query ?: "")
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText.isNullOrBlank()) {
            loadPagingData()
        }

        return true
    }
}