package me.newbly.myapplication.fragments

import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.newbly.myapplication.adapters.AnimeEpisodeListAdapter
import me.newbly.myapplication.adapters.AnimeListAdapter
import me.newbly.myapplication.adapters.PaginationScrollListener
import me.newbly.myapplication.databinding.FragmentAnimeDetailsBinding
import me.newbly.myapplication.viewmodels.AnimeDetailsViewModel

class AnimeDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = AnimeDetailsFragment()
    }

    private val viewModel: AnimeDetailsViewModel by viewModels()
    private val args: AnimeDetailsFragmentArgs by navArgs()
    private lateinit var binding: FragmentAnimeDetailsBinding
    private lateinit var episodeListAdapter: AnimeEpisodeListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentAnimeDetailsBinding.inflate(layoutInflater)
        episodeListAdapter = AnimeEpisodeListAdapter()

        prepareRecyclerView()
        viewModel.getEpisodeList(args.animeData.malId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Picasso.get().load(args.animeData.images.jpg.largeImageUrl).into(binding.animePoster)
        binding.engTitle.text = args.animeData.titleEnglish ?: args.animeData.title
        binding.japTitle.text = args.animeData.titleJapanese
        binding.episodes.text = "${args.animeData.episodes ?: "?"} episodes"
        binding.year.text = (args.animeData.year ?: "").toString()
        binding.synopsis.text = args.animeData.synopsis

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collectLatest {
                state ->
                    binding.episodeList.post {
                        episodeListAdapter.addToEpisodeList(state.episodeList)
                        episodeListAdapter.notifyItemRangeInserted(0, state.episodeList.count())
                    }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {
                    when (it.isLoading) {
                        true -> binding.episodeProgressBar.visibility = View.VISIBLE
                        false -> binding.episodeProgressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun prepareRecyclerView() {
        val recyclerView = binding.episodeList
        episodeListAdapter = AnimeEpisodeListAdapter()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = episodeListAdapter
        }
    }
}