package me.newbly.myapplication.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.newbly.myapplication.model.AnimeData
import me.newbly.myapplication.repository.JikanRepository

private const val LAST_SEARCH_QUERY = "last_search_query"
private const val LAST_QUERY_SCROLLED = "last_query_scrolled"
private const val DEFAULT_QUERY = ""

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
@HiltViewModel
class AnimeListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: JikanRepository
) : ViewModel() {
    sealed class UiAction {
        data class Search(val query: String) : UiAction()
        data class Scroll(val currentQuery: String) : UiAction()
    }

    data class UiState(
        val query: String = DEFAULT_QUERY,
        val lastQueryScrolled: String = DEFAULT_QUERY,
        val hasNotScrolledForCurrentSearch: Boolean = false
    )

    val state: StateFlow<UiState>
    val accept: (UiAction) -> Unit
    val pagingDataFlow: Flow<PagingData<AnimeData>>

    init {
        val initialQuery: String = savedStateHandle[LAST_SEARCH_QUERY] ?: DEFAULT_QUERY
        val lastQueryScrolled: String = savedStateHandle[LAST_QUERY_SCROLLED] ?: DEFAULT_QUERY
        val actionStateFlow = MutableSharedFlow<UiAction>()
        val searches = actionStateFlow
            .filterIsInstance<UiAction.Search>()
            .distinctUntilChanged()
            .onStart { emit(UiAction.Search(query = initialQuery)) }
        val queriesScrolled = actionStateFlow
            .filterIsInstance<UiAction.Scroll>()
            .distinctUntilChanged()
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                replay = 1
            )
            .onStart { emit(UiAction.Scroll(lastQueryScrolled)) }

        pagingDataFlow = searches
            .flatMapLatest { searchAnime(it.query) }
            .cachedIn(viewModelScope)

        state = combine(
            searches,
            queriesScrolled,
            ::Pair
        ).map { (search, scroll) ->
            UiState(
                query = search.query,
                lastQueryScrolled = scroll.currentQuery,
                hasNotScrolledForCurrentSearch = search.query != scroll.currentQuery
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = UiState()
            )

        accept = { action -> viewModelScope.launch { actionStateFlow.emit(action) }}
    }

    override fun onCleared() {
        savedStateHandle[LAST_SEARCH_QUERY] = state.value.query
        savedStateHandle[LAST_QUERY_SCROLLED] = state.value.lastQueryScrolled

        super.onCleared()
    }

    private fun searchAnime(query: String = ""): Flow<PagingData<AnimeData>> = repository.getAnimeListResult(query)
}