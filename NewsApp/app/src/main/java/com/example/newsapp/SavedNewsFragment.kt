package com.example.newsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.databinding.FragmentSavedNewsBinding

class SavedNewsFragment : Fragment() {

    private var _binding: FragmentSavedNewsBinding? = null
    private val binding get() = _binding!!

    private val savedNewsAdapter by lazy { NewsAdapter() }

    private val viewModel: NewsViewModel by viewModels {
        NewsViewModelFactory(DatabaseProvider.getDatabase(requireContext()))
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = savedNewsAdapter
        loadSavedNews()
    }

    private fun loadSavedNews() {
        viewModel.savedArticles.observe(viewLifecycleOwner) { articles ->
            if (articles.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Нет сохранённых новостей", Toast.LENGTH_SHORT).show()
            } else {
                savedNewsAdapter.submitList(articles)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
