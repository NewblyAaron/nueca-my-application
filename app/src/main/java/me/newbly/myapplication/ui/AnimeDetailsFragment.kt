package me.newbly.myapplication.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.newbly.myapplication.adapters.EpisodeListAdapter
import me.newbly.myapplication.databinding.FragmentAnimeDetailsBinding
import me.newbly.myapplication.model.EpisodeData

@AndroidEntryPoint
class AnimeDetailsFragment : Fragment() {
    private val viewModel: AnimeDetailsViewModel by viewModels<AnimeDetailsViewModel>(extrasProducer = {
        defaultViewModelCreationExtras.withCreationCallback<AnimeDetailsViewModel.Factory> { factory ->
            factory.create(args.animeData.malId)
        }
    })
    private val args: me.newbly.myapplication.ui.AnimeDetailsFragmentArgs by navArgs()
    private lateinit var binding: FragmentAnimeDetailsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentAnimeDetailsBinding.inflate(layoutInflater)

        val episodeListAdapter = EpisodeListAdapter()
        binding.bindList(episodeListAdapter, viewModel.pagingDataFlow)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                episodeListAdapter.loadStateFlow.collect {
                    binding.episodeProgressBar.isVisible =
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
    }

    private fun FragmentAnimeDetailsBinding.bindList(
        episodeListAdapter: EpisodeListAdapter,
        pagingDataFlow: Flow<PagingData<EpisodeData>>
    ) {
        binding.episodeList.apply {
            adapter = episodeListAdapter
            layoutManager = LinearLayoutManager(context)
        }

        lifecycleScope.launch {
            pagingDataFlow.collectLatest(episodeListAdapter::submitData)
        }
    }
}