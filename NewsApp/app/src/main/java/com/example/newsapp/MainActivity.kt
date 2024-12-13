package com.example.newsapp

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = _binding!!

    private var adapter: NewsAdapter? = null

    private val viewModel: NewsViewModel by lazy {
        NewsViewModelFactory().create(NewsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = NewsAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
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
        viewModel.newsListUI.observe(this) { state ->
            when (state) {
                is NewsListUI.Success -> adapter?.submitList(state.newsList)
                is NewsListUI.Error -> handleError(state.errorMessage)
                is NewsListUI.Empty -> handleEmptyState()
                is NewsListUI.Loading -> binding.progressBar.isVisible = state.isLoading
            }
        }
    }

    private fun handleEmptyState() {
        Toast.makeText(this, "Список новостей пуст", Toast.LENGTH_SHORT).show()
    }

    private fun handleError(@StringRes errorMessage: Int) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}