package com.example.newsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.databinding.FragmentNewsBinding

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewsViewModel by viewModels {
        NewsViewModelFactory(DatabaseProvider.getDatabase(requireContext()))
    }


    private val newsAdapter by lazy { NewsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = newsAdapter

        observeNews()
    }

    private fun observeNews() {
        viewModel.newsListUI.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NewsListUI.Success -> {
                    binding.progressBar.visibility = View.GONE
                    newsAdapter.submitList(state.newsList)
                }
                is NewsListUI.Loading -> {
                    binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
                }
                is NewsListUI.Error -> {
                    binding.progressBar.visibility = View.GONE
                    // Покажите сообщение об ошибке
                    Toast.makeText(requireContext(), getString(state.errorMessage), Toast.LENGTH_SHORT).show()
                }
                is NewsListUI.Empty -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Список новостей пуст", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
