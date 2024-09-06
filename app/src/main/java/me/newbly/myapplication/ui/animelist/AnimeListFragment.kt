package me.newbly.myapplication.ui.animelist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.newbly.myapplication.AnimeListContract
import me.newbly.myapplication.MyApplication
import me.newbly.myapplication.adapters.AnimeListAdapter
import me.newbly.myapplication.adapters.AnimeListLoadStateAdapter
import me.newbly.myapplication.databinding.FragmentAnimeListBinding
import me.newbly.myapplication.model.AnimeListModel
import me.newbly.myapplication.model.datamodel.AnimeData

class AnimeListFragment : Fragment(), AnimeListContract.View {

    private lateinit var binding: FragmentAnimeListBinding
    private lateinit var presenter: AnimeListContract.Presenter
    private lateinit var animeListAdapter: AnimeListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentAnimeListBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val service = (requireActivity().application as MyApplication).service
        val model = AnimeListModel(service)
        presenter = AnimeListPresenter(this, model)

        animeListAdapter = AnimeListAdapter(presenter)
        binding.bindList(animeListAdapter)
        binding.bindSearch()

        presenter.loadPagingData()

        return binding.root
    }

    override fun submitPagingData(pagingDataFlow: Flow<PagingData<AnimeData>>) {
        lifecycleScope.launch {
            pagingDataFlow.collectLatest { animeListAdapter.submitData(it) }
        }
    }

    override fun retryFetchingData() {
        animeListAdapter.retry()
    }

    override fun showAnimeDetails(animeData: AnimeData) {
        val action =
            AnimeListFragmentDirections.actionAnimeListFragmentToAnimeDetailsFragment(
                animeData
            )
        findNavController().navigate(action)
    }

    private fun FragmentAnimeListBinding.bindList(
        animeListAdapter: AnimeListAdapter
    ) {
        animeList.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = animeListAdapter.withLoadStateHeaderAndFooter(
                header = AnimeListLoadStateAdapter { animeListAdapter.retry() },
                footer = AnimeListLoadStateAdapter { animeListAdapter.retry() },
            )
        }
        fragmentAnimeListRetryButton.setOnClickListener { presenter.onRetryButtonClick() }

        lifecycleScope.launch {
            animeListAdapter.loadStateFlow.collect { loadState ->
                val isListEmpty = loadState.refresh is LoadState.NotLoading && animeListAdapter.itemCount == 0
                fragmentAnimeListProgressBar.isVisible = loadState.source.refresh is LoadState.Loading
                fragmentAnimeListRetryButton.isVisible = loadState.source.refresh is LoadState.Error
                fragmentAnimeListErrorText.isVisible = loadState.source.refresh is LoadState.Error
                animeList.isVisible = !isListEmpty

                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    fragmentAnimeListErrorText.text = it.error.localizedMessage
                    Toast.makeText(
                        this@AnimeListFragment.context,
                        "${it.error}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun FragmentAnimeListBinding.bindSearch() {
        animeSearchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                animeSearchView.clearFocus()
                return presenter.onQueryTextSubmit(query)
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return presenter.onQueryTextChange(newText)
            }
        })
    }
}