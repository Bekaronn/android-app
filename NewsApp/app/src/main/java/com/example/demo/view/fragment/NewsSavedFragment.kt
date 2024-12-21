package com.example.demo.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demo.databinding.FragmentNewsSavedBinding
import com.example.demo.view.adapter.NewsAdapter
import com.example.demo.viewmodel.NewsDetailsUI
import com.example.demo.viewmodel.NewsDetailsViewModel
import com.example.demo.viewmodel.NewsDetailsViewModelFactory

class NewsSavedFragment : Fragment() {

    companion object {
        fun newInstance() = NewsSavedFragment()
    }

    private var _binding: FragmentNewsSavedBinding? = null
    private val binding: FragmentNewsSavedBinding get() = _binding!!

    private var adapter: NewsAdapter? = null

    private val viewModel: NewsDetailsViewModel by lazy {
        NewsDetailsViewModelFactory().create(NewsDetailsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsSavedBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("NewsSavedFragment", "Adapter initialized")

        adapter = NewsAdapter(
            onArticleClickListener = {
                // nothing to do
            },
            onChangeFavouriteState = { article, isFavourite ->
                if (isFavourite) {
                    viewModel.removeFromFavourite(article)
                }
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        configureObserver()

        Log.d("NewsSavedFragment", "Fetching favourite news list...")
        viewModel.fetchFavouriteNewsList()
    }

    private fun configureObserver() {
        viewModel.newsDetailsUI.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NewsDetailsUI.Success -> {
                    Log.d("NewsSavedFragment", "News list fetched successfully, size: ${state.newsList.size}")
                    adapter?.submitList(state.newsList)
                }
                is NewsDetailsUI.NewsRemoved -> {
                    adapter?.removeArticle(state.article)
                }
                else -> {
                    Log.d("NewsSavedFragment", "State not handled: $state")
                }
            }
        }
    }
}