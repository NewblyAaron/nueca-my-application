package me.newbly.myapplication.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.newbly.myapplication.adapters.AnimeListAdapter
import me.newbly.myapplication.adapters.AnimeListClickListener
import me.newbly.myapplication.adapters.PaginationScrollListener
import me.newbly.myapplication.databinding.FragmentAnimeListBinding
import me.newbly.myapplication.fragments.AnimeListFragmentDirections
import me.newbly.myapplication.models.AnimeData
import me.newbly.myapplication.viewmodels.AnimeListViewModel

class AnimeListFragment : Fragment(), AnimeListClickListener {

    companion object {
        fun newInstance() = AnimeListFragment()
    }

    private val viewModel: AnimeListViewModel by viewModels()
    private lateinit var binding: FragmentAnimeListBinding
    private lateinit var animeListAdapter: AnimeListAdapter
    private lateinit var listener: AnimeListClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentAnimeListBinding.inflate(layoutInflater)
        listener = this

        prepareRecyclerView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {
                    state ->
                        when (state.isLoading) {
                            true -> binding.progressBar.visibility = View.VISIBLE
                            else -> binding.progressBar.visibility = View.GONE
                        }
                }
            }
        }
    }

    private fun prepareRecyclerView() {
        val recyclerView = binding.rvAnimeList
        animeListAdapter = AnimeListAdapter(listener)
        recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = animeListAdapter
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.state.collect { state ->
                    Log.d("AnimeListFragment", "collecting latest data")
                    recyclerView.post {
                        animeListAdapter.setAnimeList(state.animeList)
                        animeListAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

        val paginationOnScrollListener = object : PaginationScrollListener() {
            override fun isLoading(): Boolean = viewModel.state.value.isLoading

            override fun loadMoreItems() {
                viewModel.loadNextPage()
            }

        }
        recyclerView.addOnScrollListener(paginationOnScrollListener)
    }

    override fun onAnimeListItemClick(view: View, data: AnimeData) {
        val action = AnimeListFragmentDirections.actionAnimeListFragmentToAnimeDetailsFragment(data)
        findNavController().navigate(action)
    }
}