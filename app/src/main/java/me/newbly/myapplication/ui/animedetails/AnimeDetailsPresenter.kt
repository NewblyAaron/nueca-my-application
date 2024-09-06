package me.newbly.myapplication.ui.animedetails

import me.newbly.myapplication.AnimeDetailsContract

class AnimeDetailsPresenter(
    private val view: AnimeDetailsContract.View,
    private val model: AnimeDetailsContract.Model
) : AnimeDetailsContract.Presenter {
    override fun loadPagingData(animeId: Int) {
        val pager = model.getPager(animeId)
        val pagingData = pager.flow
        view.submitPagingData(pagingData)
    }
}