package me.newbly.myapplication.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.newbly.myapplication.R
import me.newbly.myapplication.viewmodels.AnimeDetailsViewModel

class AnimeDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = AnimeDetailsFragment()
    }

    private val viewModel: AnimeDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_anime_details, container, false)
    }
}