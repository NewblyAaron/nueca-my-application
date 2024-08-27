package me.newbly.myapplication.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import me.newbly.myapplication.R
import me.newbly.myapplication.adapters.AnimeListAdapter
import me.newbly.myapplication.databinding.FragmentAnimeListBinding
import me.newbly.myapplication.viewmodels.AnimeListViewModel

class AnimeListFragment : Fragment() {
    private lateinit var binding: FragmentAnimeListBinding
    private lateinit var animeListAdapter: AnimeListAdapter

    companion object {
        fun newInstance() = AnimeListFragment()
    }

    private val viewModel: AnimeListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentAnimeListBinding.inflate(layoutInflater)

        prepareRecyclerView()
        viewModel.getAnimeList()
        viewModel.observeAnimeListLiveData().observe(this
        ) { animeList -> animeListAdapter.setAnimeList(animeList) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    private fun prepareRecyclerView() {
        animeListAdapter = AnimeListAdapter()
        binding.rvAnimeList.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = animeListAdapter
        }
    }
}