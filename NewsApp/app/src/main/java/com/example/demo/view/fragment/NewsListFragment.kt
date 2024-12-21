package com.example.demo.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demo.databinding.FragmentNewsListBinding
import com.example.demo.model.entity.Article
import com.example.demo.view.adapter.NewsAdapter
import com.example.demo.viewmodel.NewsListUI
import com.example.demo.viewmodel.NewsViewModel
import com.example.demo.viewmodel.NewsViewModelFactory


class NewsListFragment : Fragment() {

    companion object {
        fun newInstance() = NewsListFragment()
    }

    private var _binding: FragmentNewsListBinding? = null
    private val binding: FragmentNewsListBinding get() = _binding!!

    private var adapter: NewsAdapter? = null

    private val viewModel: NewsViewModel by lazy {
        NewsViewModelFactory().create(NewsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        adapter = NewsAdapter(
//            onArticleClickListener = {
////                val newsDetailsFragment = ArticleDetailsFragment.newInstance(it.title)
////
////                requireActivity().supportFragmentManager
////                    .beginTransaction()
////                    .replace(R.id.fragment_container_view, newsDetailsFragment)
////                    .addToBackStack(null)
////                    .commit()
////            },
//            onChangeFavouriteState = { article, isFavourite ->
////                viewModel.changeFavouriteState(news, isFavourite)
//            }
//
//        )
        adapter = NewsAdapter(
            onArticleClickListener = {
                // nothing to do
            },
            onChangeFavouriteState = { article, isFavourite ->
                viewModel.changeFavouriteState(article, isFavourite)
            }
        )

        binding.recyclerView.adapter = adapter

        configureSearch()
        configureObserver()

        viewModel.fetchNewsList()
    }

    private fun configureSearch() {
        binding.searchEditText.setOnEditorActionListener { textView, actionId, event ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH ||
                event?.action == android.view.KeyEvent.ACTION_DOWN) {
                val query = textView.text.toString()
                viewModel.searchNews(query)
                true
            } else {
                false
            }
        }
    }

    private fun configureObserver() {
        viewModel.newsListUI.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NewsListUI.Success -> adapter?.submitList(state.newsList)
                is NewsListUI.Error -> handleError(state.errorMessage)
                is NewsListUI.Empty -> handleEmptyState()
                is NewsListUI.Loading -> binding.progressBar.isVisible = state.isLoading
                is NewsListUI.NewsInserted -> handleArticleInsert(state.article)
                is NewsListUI.NewsIsAlreadyFavourite -> Toast.makeText(
                    requireContext(), "News is already favourite", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun handleArticleInsert(article: Article) {
        Toast.makeText(requireContext(), "Article saved!", Toast.LENGTH_SHORT).show()

        adapter?.submitList(
            adapter?.currentList?.map {
                if (it.id == article.id) {
                    article
                } else {
                    it
                }
            }
        )
    }

    private fun handleEmptyState() {
        // TODO: Handle UI for case when there is no news list
//        Toast.makeText(this, "Список новостей пуст", Toast.LENGTH_SHORT).show()
    }

    private fun handleError(@StringRes errorMessage: Int) {
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
    }
}