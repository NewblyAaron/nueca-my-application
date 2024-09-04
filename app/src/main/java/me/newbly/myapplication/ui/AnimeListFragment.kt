package me.newbly.myapplication.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
import me.newbly.myapplication.adapters.AnimeListLoadStateAdapter
import me.newbly.myapplication.databinding.FragmentAnimeListBinding
import me.newbly.myapplication.model.AnimeData

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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
            layoutManager = GridLayoutManager(context, 2)
            adapter = animeListAdapter.withLoadStateHeaderAndFooter(
                header = AnimeListLoadStateAdapter { animeListAdapter.retry() },
                footer = AnimeListLoadStateAdapter { animeListAdapter.retry() },
            )
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
                if (newText.isNullOrBlank()) {
                    updateAnimeListFromInput(onQueryChanged)
                }
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
        binding.fragmentAnimeListRetryButton.setOnClickListener { animeListAdapter.retry() }
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

        lifecycleScope.launch {
            animeListAdapter.loadStateFlow.collect { loadState ->
                val isListEmpty = loadState.refresh is LoadState.NotLoading && animeListAdapter.itemCount == 0
                binding.fragmentAnimeListProgressBar.isVisible = loadState.source.refresh is LoadState.Loading
                binding.fragmentAnimeListRetryButton.isVisible = loadState.source.refresh is LoadState.Error
                binding.fragmentAnimeListErrorText.isVisible = loadState.source.refresh is LoadState.Error
                binding.animeList.isVisible = !isListEmpty

                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    binding.fragmentAnimeListErrorText.text = it.error.localizedMessage
                    Toast.makeText(
                        this@AnimeListFragment.context,
                        "${it.error}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun FragmentAnimeListBinding.updateAnimeListFromInput(
        onQueryChanged: (AnimeListViewModel.UiAction.Search) -> Unit) {
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