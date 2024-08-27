package me.newbly.myapplication.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import me.newbly.myapplication.databinding.FragmentAnimeDetailsBinding
import me.newbly.myapplication.viewmodels.AnimeDetailsViewModel

class AnimeDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = AnimeDetailsFragment()
    }

    private val viewModel: AnimeDetailsViewModel by viewModels()
    private val args: AnimeDetailsFragmentArgs by navArgs()
    private lateinit var binding: FragmentAnimeDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentAnimeDetailsBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Picasso.get().load(args.animeData.images.jpg.largeImageUrl).into(binding.animePoster)
        binding.engTitle.text = args.animeData.titleEnglish ?: args.animeData.title
        binding.japTitle.text = args.animeData.titleJapanese
        binding.year.text = (args.animeData.year ?: "").toString()
    }
}