package com.makertech.assignmentquantamit.ui.news

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.makertech.assignmentquantamit.R
import com.makertech.assignmentquantamit.data.remote.NewsApi
import com.makertech.assignmentquantamit.databinding.ActivityNewsListBinding
import com.makertech.assignmentquantamit.other.State
import com.makertech.assignmentquantamit.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NewsListActivity:AppCompatActivity(), SearchView.OnQueryTextListener {
    private lateinit var binding: ActivityNewsListBinding
    private val viewModel: NewsViewModel by viewModels()

    @Inject
    lateinit var api:NewsApi

    lateinit var newsAdapter: NewsListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.searchView.setOnQueryTextListener(this)

        viewModel.getNewsArticles().observe(this) {stateNewsArticles->
            when (stateNewsArticles) {
                is State.Loading -> {
                }
                is State.Success -> {
                    newsAdapter = NewsListAdapter(stateNewsArticles.data)

                    binding.apply {
                        newsRecyclerview.apply {
                            adapter = newsAdapter
                            layoutManager = LinearLayoutManager(this@NewsListActivity)
                        }
                    }
                }
            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(newText != null){
            searchDatabase(newText)
        }
        return true
    }

    private fun searchDatabase(query: String) {
        val searchQuery = "%$query%"

        viewModel.searchDatabase(searchQuery).observe(this) { list ->
            list.let {
                newsAdapter.setData(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_logout, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> {
                logout()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(applicationContext, MainActivity::class.java))
    }

}