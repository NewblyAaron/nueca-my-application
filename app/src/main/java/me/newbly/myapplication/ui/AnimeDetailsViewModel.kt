package me.newbly.myapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import me.newbly.myapplication.model.EpisodeData
import me.newbly.myapplication.repository.JikanRepository

@HiltViewModel(
    assistedFactory = AnimeDetailsViewModel.Factory::class
)
class AnimeDetailsViewModel @AssistedInject constructor(
    repository: JikanRepository,
    @Assisted private val animeId: Int
) : ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(animeId: Int) : AnimeDetailsViewModel
    }

    val pagingDataFlow: Flow<PagingData<EpisodeData>> = repository.getEpisodeList(animeId).cachedIn(viewModelScope)
}

