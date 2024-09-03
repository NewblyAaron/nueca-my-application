package me.newbly.myapplication.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.newbly.myapplication.adapters.AnimeListAdapter
import me.newbly.myapplication.adapters.AnimeListClickListener
import me.newbly.myapplication.databinding.FragmentAnimeListBinding
import me.newbly.myapplication.model.AnimeData
import me.newbly.myapplication.ui.AnimeDetailsFragmentDirections

@AndroidEntryPoint
class AnimeListFragment : Fragment(), AnimeListClickListener {
    private val viewModel: AnimeListViewModel by viewModels()
    private lateinit var binding: FragmentAnimeListBinding
    private lateinit var animeListAdapter: AnimeListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("AnimeListFragment", "onCreate")

        binding = FragmentAnimeListBinding.inflate(layoutInflater)
        animeListAdapter = AnimeListAdapter(this)

        binding.bindState(
            uiState = viewModel.state,
            pagingData = viewModel.pagingDataFlow,
            uiActions = viewModel.accept
        )

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                animeListAdapter.loadStateFlow.collect {
                    binding.progressBar.isVisible =
                        it.source.append is LoadState.Loading
                                || it.source.prepend is LoadState.Loading
                                || it.source.refresh is LoadState.Loading
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("AnimeListFragment", "onCreateView")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("AnimeListFragment", "onViewCreated")
    }

    override fun onAnimeListItemClick(view: View, data: AnimeData) {
        val action =
            AnimeListFragmentDirections.actionAnimeListFragmentToAnimeDetailsFragment(
                data
            )
        findNavController().navigate(action)
    }

    private fun FragmentAnimeListBinding.bindState(
        uiState: StateFlow<AnimeListViewModel.UiState>,
        pagingData: Flow<PagingData<AnimeData>>,
        uiActions: (AnimeListViewModel.UiAction) -> Unit
    ) {
        animeList.apply {
            adapter = animeListAdapter
            layoutManager = GridLayoutManager(context, 2)
        }

        bindSearch(
            uiState = uiState,
            onQueryChanged = uiActions
        )
        bindList(
            animeListAdapter = animeListAdapter,
            uiState = uiState,
            pagingData = pagingData,
            onScrollChanged = uiActions
        )
    }

    private fun FragmentAnimeListBinding.bindSearch(
        uiState: StateFlow<AnimeListViewModel.UiState>,
        onQueryChanged: (AnimeListViewModel.UiAction.Search) -> Unit
    ) {
        animeSearchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                updateAnimeListFromInput(onQueryChanged)
                animeSearchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        lifecycleScope.launch {
            uiState
                .map { it.query }
                .distinctUntilChanged()
                .collect { animeSearchView.setQuery(it, true) }
        }
    }

    private fun FragmentAnimeListBinding.bindList(
        animeListAdapter: AnimeListAdapter,
        uiState: StateFlow<AnimeListViewModel.UiState>,
        pagingData: Flow<PagingData<AnimeData>>,
        onScrollChanged: (AnimeListViewModel.UiAction.Scroll) -> Unit
    ) {
        animeList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy != 0) onScrollChanged(AnimeListViewModel.UiAction.Scroll(currentQuery = uiState.value.query))
            }
        })

        val notLoading = animeListAdapter.loadStateFlow
            .distinctUntilChangedBy { it.source.refresh }
            .map { it.source.refresh is LoadState.NotLoading }

        val hasNotScrolledForCurrentSearch = uiState
            .map { it.hasNotScrolledForCurrentSearch }
            .distinctUntilChanged()

        val shouldScrollToTop = combine(
            notLoading,
            hasNotScrolledForCurrentSearch,
            Boolean::and
        )
            .distinctUntilChanged()

        lifecycleScope.launch {
            pagingData.collectLatest(animeListAdapter::submitData)
        }

        lifecycleScope.launch {
            shouldScrollToTop.collect { shouldScroll ->
                if (shouldScroll) animeList.scrollToPosition(0)
            }
        }
    }

    private fun FragmentAnimeListBinding.updateAnimeListFromInput(onQueryChanged: (AnimeListViewModel.UiAction.Search) -> Unit) {
        animeSearchView.query.trim().let {
            if (it.isNotEmpty()) {
                animeList.scrollToPosition(0)
                onQueryChanged(AnimeListViewModel.UiAction.Search(query = it.toString()))
            } else {
                // query nothing
                onQueryChanged(AnimeListViewModel.UiAction.Search(query = ""))
            }
        }
    }
}