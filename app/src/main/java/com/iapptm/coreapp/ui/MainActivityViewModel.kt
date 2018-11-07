package com.iapptm.coreapp.ui


import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iapptm.coreapp.base.BaseViewModel

import com.iapptm.coreapp.data.models.MovieModel
import com.iapptm.coreapp.data.services.ApiService
import com.iapptm.coreapp.data.services.IRxSchedulers
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext
import timber.log.Timber


class MainActivityViewModel(context: Context) : BaseViewModel(), KodeinAware {
    override val kodein: Kodein by closestKodein(context)
    override val kodeinContext = kcontext(context)
    private val apiService: ApiService by instance()
    private val schedulers: IRxSchedulers by instance()
    var movies: MutableLiveData<List<MovieModel>> = MutableLiveData()

    fun fetchMovies() {
        addDisposable(apiService.movies()
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main())
                .subscribe({ response -> movies.postValue(response) }, { Timber.e(it) })
        )
    }

    class Factory(val context: Context) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainActivityViewModel(context) as T
        }
    }
}