package me.newbly.myapplication.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.newbly.myapplication.models.EpisodeData
import me.newbly.myapplication.repositories.JikanRepository

class AnimeDetailsViewModel : ViewModel() {
    private val repository = JikanRepository()

    data class UiState(
        val isLoading: Boolean = true,
        val episodeList: List<EpisodeData> = emptyList()
    )

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState>
        get() = _state.asStateFlow()

    fun getEpisodeList(animeId: Int) {
        viewModelScope.launch {
            _state.update { UiState() }
            repository.getEpisodeList(animeId)
                .catch { e -> Log.d("AnimeDetailsViewModel", e.message, e) }
                .collect{
                    list ->
                        _state.update {
                            Log.d("AnimeDetailsViewModel", "fetched episodes")
                            it.copy(isLoading = false, episodeList = list)
                        }
                }
        }
    }
}