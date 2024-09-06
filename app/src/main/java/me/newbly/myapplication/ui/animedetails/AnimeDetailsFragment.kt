package me.newbly.myapplication.ui.animedetails

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.newbly.myapplication.AnimeDetailsContract
import me.newbly.myapplication.MyApplication
import me.newbly.myapplication.adapters.EpisodeListAdapter
import me.newbly.myapplication.databinding.FragmentAnimeDetailsBinding
import me.newbly.myapplication.model.EpisodeListModel
import me.newbly.myapplication.model.datamodel.EpisodeData

class AnimeDetailsFragment : Fragment(), AnimeDetailsContract.View {

    private lateinit var binding: FragmentAnimeDetailsBinding
    private lateinit var presenter: AnimeDetailsContract.Presenter
    private lateinit var episodeListAdapter: EpisodeListAdapter

    private val args: AnimeDetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentAnimeDetailsBinding.inflate(layoutInflater)

        episodeListAdapter = EpisodeListAdapter()
        binding.episodeList.apply {
            adapter = episodeListAdapter
            layoutManager = LinearLayoutManager(context)
        }
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
        val service = (requireActivity().application as MyApplication).service
        val model = EpisodeListModel(service)
        presenter = AnimeDetailsPresenter(this, model)

        presenter.loadPagingData(args.animeData.malId)
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

    override fun submitPagingData(pagingDataFlow: Flow<PagingData<EpisodeData>>) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                pagingDataFlow.collectLatest { episodeListAdapter.submitData(it) }
            }
        }
    }
}