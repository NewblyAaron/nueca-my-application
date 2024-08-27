package me.newbly.myapplication.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import me.newbly.myapplication.adapters.AnimeListAdapter
import me.newbly.myapplication.adapters.AnimeListClickListener
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
        animeListAdapter = AnimeListAdapter(listener)
        binding.rvAnimeList.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = animeListAdapter
        }
    }

    override fun onAnimeListItemClick(view: View, data: AnimeData) {
        val action = AnimeListFragmentDirections.actionAnimeListFragmentToAnimeDetailsFragment(data)
        findNavController().navigate(action)
    }
}