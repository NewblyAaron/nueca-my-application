package me.newbly.myapplication.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.newbly.myapplication.models.AnimeData
import me.newbly.myapplication.repositories.JikanRepository

class AnimeListViewModel : ViewModel() {
    private val repository = JikanRepository()

    data class UiState(
        val isLoading: Boolean = true,
        val animeList: List<AnimeData> = emptyList(),
        val pageIndex: Int = 0
    )

    init {
        getAnimeList(1)
    }

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState>
        get() = _state.asStateFlow()

    private fun getAnimeList(pageIndex: Int) {
        viewModelScope.launch {
            Log.d("AnimeListViewModel", "fetching data from jikan")
            _state.update { UiState(pageIndex = pageIndex) }
            repository.getAnimeList(pageIndex)
                .catch { e -> Log.d("AnimeListViewModel", e.message, e) }
                .collect {
                    list ->
                        _state.update {
                            val newAnimeList = _state.value.animeList.toMutableList()
                            newAnimeList.addAll(list)
                            Log.d("AnimeListViewModel", "fetched new data")
                            it.copy(animeList = newAnimeList, isLoading = false)
                        }
                    }
        }
    }

    fun loadNextPage() {
        val nextPageIndex = _state.value.pageIndex + 1
        getAnimeList(nextPageIndex)
    }
}