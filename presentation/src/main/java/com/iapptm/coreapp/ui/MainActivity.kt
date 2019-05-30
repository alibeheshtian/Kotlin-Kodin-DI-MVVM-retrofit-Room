package com.iapptm.coreapp.ui

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.iapptm.coreapp.R
import com.iapptm.coreapp.adapters.MoviesAdapters
import com.shenoto.domain.bases.BaseActivity
import com.example.data.database.entity.MovieEntity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(layout = R.layout.activity_main) {
    private lateinit var viewModel: MainActivityViewModel

    private val adapters = MoviesAdapters {
        viewModel.movieAddToFavorite(MovieEntity(it.id, it.title))
    }

    override fun viewIsReady(savedInstanceState: Bundle?) {
        rv_movies.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = adapters
        }

        viewModel = ViewModelProviders
            .of(this, MainActivityViewModel.Factory(this))
            .get(MainActivityViewModel::class.java)

        viewModel.movies.observe(this, Observer {
            adapters.submitList(it)
        })
        viewModel.moviesStored.observe(this, Observer {
            title=it.size.toString()
        })

        viewModel.fetchMovies()

        viewModel.loading.observe(this, Observer { customLoading().toggel(it) })


    }
}
