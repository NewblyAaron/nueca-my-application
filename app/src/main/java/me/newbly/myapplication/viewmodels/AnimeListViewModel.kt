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
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.newbly.myapplication.models.AnimeData
import me.newbly.myapplication.repositories.JikanRepository

class AnimeListViewModel : ViewModel() {
    private val repository = JikanRepository()

    data class UiState(
        val isLoading: Boolean = true,
        val animeList: MutableList<AnimeData> = mutableListOf(),
        val pageIndex: Int = 1
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
            _state.update { it.copy(isLoading = true) }
            repository.getAnimeList(pageIndex)
                .catch { e -> Log.d("AnimeListViewModel", e.message, e) }
                .collect {
                    list ->
                        _state.update {
                            val newAnimeList = _state.value.animeList.toMutableList()
                            newAnimeList.addAll(list)
                            it.copy(animeList = newAnimeList)
                        }
                    }
            _state.update { it.copy(isLoading = false) }
        }
    }

    fun loadNextPage() {
        getAnimeList(state.value.pageIndex)
    }
}