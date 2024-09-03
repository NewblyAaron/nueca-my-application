package me.newbly.myapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import me.newbly.myapplication.model.EpisodeData
import me.newbly.myapplication.repository.JikanRepository

class AnimeDetailsViewModel @AssistedInject constructor(
    repository: JikanRepository,
    @Assisted private val animeId: Int
) : ViewModel() {
    val pagingDataFlow: Flow<PagingData<EpisodeData>>

    init {
        pagingDataFlow = repository.getEpisodeList(animeId).cachedIn(viewModelScope)
    }
}

@AssistedFactory
interface AnimeDetailsViewModelFactory {
    fun create(animeId: Int) : AnimeDetailsViewModel
}