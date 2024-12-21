package com.example.demo.view.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.demo.R
import com.example.demo.databinding.ActivityMainBinding
import com.example.demo.view.fragment.NewsListFragment
import com.example.demo.view.fragment.NewsSavedFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val newsSavedFragment = NewsSavedFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val newsListFragment = NewsListFragment.newInstance()

        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container_view, newsListFragment)
            .commit()

        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.news_list -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container_view, newsListFragment)
                        .commit()
                }

                R.id.saved_news -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container_view, newsSavedFragment)
                        .commit()
                }
            }

            true
        }
    }

}